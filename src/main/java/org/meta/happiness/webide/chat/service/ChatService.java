package org.meta.happiness.webide.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.chat.dto.ChatMessageDto;
import org.meta.happiness.webide.chat.dto.ChatMessageType;
import org.meta.happiness.webide.chat.entity.ChatMessage;
import org.meta.happiness.webide.chat.repository.ChatMessageRepository;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.repository.user.UserRepository;
import org.meta.happiness.webide.repository.userrepo.UserRepoRepository;
import org.springframework.context.event.EventListener;
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
    private final UserRepoRepository userRepoRepository;

    // 필요한 메서드: 메시지 전송/저장
    // 메시지 전송
    private void sendMessage(ChatMessageDto message) {
        log.info("현재 레포 ID: {}", message.getRepoId());
        log.info("메시지 내용: {}", message.getContent());
        messagingTemplate.convertAndSend("sub/repo/" + message.getRepoId(), message);
    }
    // 메시지 저장
    private void saveMessage(ChatMessageDto message) {
        log.info("보낸 사람 닉네임: {}", message.getSender());
        User user = userRepository.findByNickname(message.getSender())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다"));
//        Repo repo = userRepoRepository.;

        // Todo: 나중에 해야 함
        ChatMessage chatMessage = ChatMessage.builder()
                .chatMessageType(message.getType())
                .messageContent(message.getContent()).build();


    }



    // 세션이 종료 이벤트가 감지되면 메시지가 전송됨
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

    // todo: 메서드 설계 -> 레포 아이디로 메시지 가져오기
//    public List<ChatMessageDto> getMessagesInRepo(Long repoId) {
//        Optional<List<ChatMessage>> messages = chatMessageRepository.findByRepoId(repoId);

//        return messages.orElseGet(ArrayList::new)
//                .stream()
//                .map(message -> ChatMessageDto.builder()
//                        .type(message.getChatMessageType())
//                        .sender(message.getUserRepo().getUser().getNickname())
//                        .content(message.getMessageContent())
//                        .createdAt(message.getCreatedDate())
//                        .repoId(message.getUserRepo().getRepo().getId())
//                        .build())
//                .toList();
//    }
}
