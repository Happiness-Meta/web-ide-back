package org.meta.happiness.webide.chat.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.chat.dto.ChatMessageRequestDto;
import org.meta.happiness.webide.chat.dto.ChatMessageResponseDto;
import org.meta.happiness.webide.chat.service.ChatService;
import org.meta.happiness.webide.dto.api.ApiResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chat", description = "채팅 관련 API")
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // 채팅방(레포) 입장 시 실행
    /*
    클라이언트가 아래 경로로 요청을 한다
    내용을 담은 메시지 객체와 레포 아이디를 담은 경로가 넘어온다.
    이 메시지를 그대로 특정 방을 구독하고 있는 사람들에게 보내준다.
    매개 변수는 MessageMapping의 repoId를 가진다.
    */
    @MessageMapping("/chat/enter/{repoId}")
    @SendTo("/sub/repo/{repoId}")
    public ChatMessageRequestDto enterRoom(
            @DestinationVariable String repoId,
            @Payload ChatMessageRequestDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // 현재 실행 중인 웹소켓 세션에 사용자 이름 넣기.
        // todo: 클라이언트에서 메시지를 받은 뒤에 어떻게 처리하는지 확인 필요

        chatService.enterRoom(repoId, chatMessage, headerAccessor);
        return chatMessage;
    }
    // 메시지를 보낼 때마다 실행
    /*
    * 아래 경로로 메시지 정보가 들어온다
    * 특정 채팅방 경로에 메시지를 뿌려준다
    * */
    @MessageMapping("/chat/send/{repoId}")
    @SendTo("/sub/repo/{repoId}")
    public ChatMessageRequestDto sendMessage(
            @DestinationVariable String repoId,
            @Payload ChatMessageRequestDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
//        chatService.sendMessage(repoId, chatMessage, headerAccessor);
        chatService.saveMessage(chatMessage, repoId);
        chatService.sendMessage(chatMessage, repoId);
        return chatMessage;
    }

    //레포 안의 전체 메시지 불러오기 <- 프론트에서 이전 메시지 불러오는 기능으로 사용되어야 함.
    @GetMapping("/{repoId}/messages")
    public ApiResponse<?> getMessagesInRepo(@PathVariable String repoId) {
        List<ChatMessageResponseDto> responseData = chatService.getMessagesInRepo(repoId);
        return ApiResponse.ok(responseData);
    }
}
