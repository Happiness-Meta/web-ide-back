package org.meta.happiness.webide.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.chat.dto.ChatMessageDto;
import org.meta.happiness.webide.chat.dto.ChatMessageType;
import org.meta.happiness.webide.chat.entity.ChatMessage;
import org.meta.happiness.webide.chat.repository.ChatMessageRepository;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.repository.user.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    // 의존성 주입
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // 필요한 메서드: 메시지 전송/저장
    // 채팅방 입장(레포당 채팅 룸은 한개씩 있기에, 사실상 레포 입장과 동일하다)
    public void enterRoom(String repoId, ChatMessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // 현재 실행 중인 웹소켓 세션 헤더에 사용자 이름 넣기.
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        log.info("{}가 입장", chatMessage.getSender());
    }

    public void saveAndSendMessage(ChatMessageDto message) {
        sendMessage(message);
        saveMessage(message);
    }
    // 메시지 전송
    private void sendMessage(ChatMessageDto message) {
        log.info("메시진 발신자: {}", message.getSender());
        log.info("메시지 내용: {}", message.getContent());
        messagingTemplate.convertAndSend("sub/repo/" + message.getRepoId(), message);
    }
    // 메시지 저장
    private void saveMessage(ChatMessageDto message) {
        log.info("저장될 메시지: {}", message);
        User user = userRepository.findByNickname(message.getSender())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다"));

        // Todo: 실제로 메시지 저장하는 부분 구현, 레포 아이디를 가지고 있어야 하는데 어디서 어떻게 반영이 되는지 알아야 한다.
        ChatMessage chatMessage = ChatMessage.builder()
                .chatMessageType(message.getType())
                .messageContent(message.getContent())
                .userId(message.getUserId())
                .repoId(message.getRepoId())
                .build();
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getMessagesInRepo(String repoId) {
        // todo: 메서드 설계 -> 레포 아이디로 메시지 가져오기
        // 데이터베이스에서 가져온 채팅 메시지
        Optional<List<ChatMessage>> messages = chatMessageRepository.findByRepoId(repoId);

        return messages.orElseGet(ArrayList::new)
                .stream()
                .map(message -> ChatMessageDto.builder()
                        .type(message.getChatMessageType())
                        .sender(message.getUserRepo().getUser().getNickname())
                        .content(message.getMessageContent())
                        .createdAt(message.getCreatedDate())
                        .repoId(message.getUserRepo().getRepo().getId())
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
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("채팅 퇴장: {}", username);
            var chatMessage = ChatMessageDto.builder()
                    .type(ChatMessageType.LEAVE)
                    .sender(username)
                    .build();
            messagingTemplate.convertAndSend("/sub/public", chatMessage);
        }
    }
}
