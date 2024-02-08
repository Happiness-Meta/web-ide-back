package org.meta.happiness.webide.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {
    private String roomId;
    private String roomName;
    private long userCount;
    private Map<String, String> userlist = new HashMap<>();

    public static ChatRoom createRoom(String roomName){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        return chatRoom;
    }
}
