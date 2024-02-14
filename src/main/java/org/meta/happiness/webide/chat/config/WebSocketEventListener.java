package org.meta.happiness.webide.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.chat.dto.ChatMessageType;
import org.meta.happiness.webide.chat.entity.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

//todo:[해결] bean 등록 해야 함. @Component 달면, messagingTemplate에서 에러가 발생하게 됨
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    // 웹소켓 세션이 종료될 때마다, 아래 메서드가 실행되어서 아래 메시지를 전달하게 됨
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("user disconnected: {}", username);
            var chatMessage = ChatMessage.builder()
                    .chatMessageType(ChatMessageType.LEAVE)
                    .build();
            messagingTemplate.convertAndSend("/sub/public", chatMessage);
        }
    }
}

