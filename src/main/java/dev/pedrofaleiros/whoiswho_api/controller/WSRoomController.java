package dev.pedrofaleiros.whoiswho_api.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WSRoomController {

    private RoomService roomService;
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/join/{room}")
    @SendToUser("/queue/joinResponse")
    public boolean joinRoom(@DestinationVariable String room, @Payload String username,
            SimpMessageHeaderAccessor headerAccessor) {
        var sessionId = headerAccessor.getSessionId();

        try {
            var updatedRoom = roomService.addUser(room, username);

            //TODO: enviar users separados

            messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + "/roomData",
                    updatedRoom);

            headerAccessor.getSessionAttributes().put("username", username);
            headerAccessor.getSessionAttributes().put("room", room);

            return true;
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage());
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/errors", e.getMessage(),
                    createHeaders(sessionId));
            return false;
        }
    }

    @MessageMapping("/update/{room}")
    public void updateRoom(@DestinationVariable String room, @Payload UpdateRoomDTO data,
            SimpMessageHeaderAccessor headerAccessor) {

        var sessionId = headerAccessor.getSessionId();
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        data.setUsername(username);
        data.setRoom(room);

        try {
            var updatedRoom = roomService.updateRoom(data);

            messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + "/roomData",
                    updatedRoom);
        } catch (RuntimeException e) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", e.getMessage(),
                    createHeaders(sessionId));
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor =
                SimpMessageHeaderAccessor.wrap(event.getMessage());

        var sessionId = headerAccessor.getSessionId();
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String room = (String) headerAccessor.getSessionAttributes().get("room");

        try {
            var updatedRoom = roomService.removeUser(room, username);
            messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + "/roomData",
                    updatedRoom);
        } catch (RuntimeException e) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", e.getMessage(),
                    createHeaders(sessionId));
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor =
                SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
