package org.meta.happiness.webide.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.chat.dto.ChatMessageDto;
import org.meta.happiness.webide.chat.service.ChatService;
import org.meta.happiness.webide.dto.api.ApiResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    //todo: 특정 레포에 한정하여 사용자 추가되는 로직 넣기
    @MessageMapping("/chat.addUser")
    @SendTo("sub/public")
//    @SendTo("/sub/repo/{repoID}")
    public ChatMessageDto addUser(
            @Payload ChatMessageDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // 현재 실행 중인 웹 소켓에 사용자 이름 넣기.
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        log.info("{} 님이 입장하셨습니다.", chatMessage.getSender());
        return chatMessage;
    }
    //todo: 특정 레포에 한정하여 메시지 전송하는 로직 넣기
    @MessageMapping("/chat.sendMessage")
    @SendTo("/sub/public")
//    @SendTo("/sub/repo/{repoID}")
    public ChatMessageDto sendMessage(
            @Payload ChatMessageDto chatMessage
    ) {
        return chatMessage;
    }

    //레포 안의 전체 메시지 불러오기 <- 프론트에서 이전 메시지 불러오는 기능으로 사용되어야 함.
//    @GetMapping("/{repoId}/messages")
//    public ApiResponse<?> getMessagesInRepo(@PathVariable Long repoId) {
//        List<ChatMessageDto> responseData = chatService.getMessagesInRepo(repoId);
//        return ApiResponse.ok(responseData);
//    }
}
