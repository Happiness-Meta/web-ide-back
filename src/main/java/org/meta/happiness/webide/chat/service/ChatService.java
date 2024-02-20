package org.meta.happiness.webide.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.chat.dto.ChatMessageRequestDto;
import org.meta.happiness.webide.chat.dto.ChatMessageResponseDto;
import org.meta.happiness.webide.chat.dto.ChatMessageType;
import org.meta.happiness.webide.chat.entity.ChatMessage;
import org.meta.happiness.webide.chat.repository.ChatMessageRepository;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.exception.RepoNotFoundException;
import org.meta.happiness.webide.exception.UserNotFoundException;
import org.meta.happiness.webide.exception.UserRepoNotFoundException;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.meta.happiness.webide.repository.user.UserRepository;
import org.meta.happiness.webide.repository.userrepo.UserRepoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    // 의존성 주입
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final UserRepoRepository userRepoRepository;
    private final RepoRepository repoRepository;

    // 필요한 메서드: 메시지 전송/저장
    // 채팅방 입장(레포당 채팅 룸은 한개씩 있기에, 사실상 레포 입장과 동일하다)
    public void enterRoom(String repoId, ChatMessageRequestDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // 현재 실행 중인 웹소켓 세션 헤더에 사용자 이름 넣기.
        headerAccessor.getSessionAttributes().put("userName", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("repoId", repoId);
        log.info("입장할 때 레포 아이디는 {}", repoId);
        log.info("입장할 때 채팅 메시지는 {}", chatMessage);
        log.info("{}가 입장", chatMessage.getSender());
    }

//    public void saveAndSendMessage(ChatMessageRequestDto message, String repoId) {
//        log.info("메시지 보낼 때 레포 아이디는 {}", repoId);
//        log.info("메시지 보낼 때 채팅 메시지는 {}", message.getContent());
//        sendMessage(message, repoId);
//        saveMessage(message, repoId);
//    }

    // 메시지 전송 todo: 지금 여기서 문제가 있는 것 같다. 메시지를 전송해주고 있지 않아. enter에서는 어떻게 메시지를 보내주고 있지?
    public void sendMessage(ChatMessageRequestDto message, String repoId) {
        log.info("메시진 발신자: {}", message.getSender());
        log.info("메시지 내용: {}", message.getContent());
        messagingTemplate.convertAndSend("sub/repo/" + repoId, message);
        log.info("메시지 변형하고 보내기 완료");
    }

    // 메시지 저장
    public void saveMessage(ChatMessageRequestDto message, String repoId) {
        log.info("저장될 메시지: {}", message.getContent());
        User user = userRepository.findByNickname(message.getSender())
                .orElseThrow(UserNotFoundException::new);
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(RepoNotFoundException::new);
        UserRepo userRepo = userRepoRepository.findByUserAndRepo(user, repo)
                .orElseThrow(UserRepoNotFoundException::new);

        // Todo: 실제로 메시지 저장하는 부분 구현, 레포 아이디를 가지고 있어야 하는데 어디서 어떻게 반영이 되는지 알아야 한다.
        // dto -> entity. 변환 방법.
        ChatMessage chatMessage = ChatMessage.createChatMessage(userRepo, message);

        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageResponseDto> getMessagesInRepo(String repoId) {
        // todo: 메서드 설계 -> 레포 아이디로 메시지 가져오기
        // 데이터베이스에서 가져온 채팅 메시지
        // repo 에서 뭘 찾음? -> userrepo
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(RepoNotFoundException::new);
        List<UserRepo> userRepos = userRepoRepository.findAllByRepo(repo);
        List<ChatMessage> messages = chatMessageRepository.findAllByUserRepoIn(userRepos)
                .orElse(Collections.emptyList());
        return messages
                .stream()
                .map(message -> ChatMessageResponseDto.builder()
                        .type(message.getChatMessageType())
                        .sender(message.getUserRepo().getUser().getNickname())
                        .content(message.getMessageContent())
                        .build())
                .toList();
    }

    // 세션이 종료 이벤트가 감지될 때 실행
    /*
     * 퇴장 메시지 전송(채팅방(레포) 접근 권한을 잃는 것은 아님)
     * 단순히 세션에서 벗어나 채팅방을 나가는 것
     * todo: 접근 권한을 가지고 있는 사람이 다시 채팅방에 접근할 때 어떻게 해야 하는지 설정 필요.
     * */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = (String) headerAccessor.getSessionAttributes().get("userName");
        String repoId = (String) headerAccessor.getSessionAttributes().get("repoId");
        if (userName != null) {
            log.info("채팅 퇴장: {}", userName);
            ChatMessageResponseDto chatMessage = ChatMessageResponseDto.builder()
                    .type(ChatMessageType.LEAVE)
                    .sender(userName)
                    .content("")
                    .build();
            messagingTemplate.convertAndSend("sub/repo/" + repoId, chatMessage);
        }
    }


}
