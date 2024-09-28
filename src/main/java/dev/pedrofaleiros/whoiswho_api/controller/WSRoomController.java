package dev.pedrofaleiros.whoiswho_api.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.service.WSRoomService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WSRoomController {

    private WSRoomService service;
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/join/{room}")
    public void joinRoom(@DestinationVariable String room, @Payload String username, SimpMessageHeaderAccessor headerAccessor) {
        
        var sessionId = headerAccessor.getSessionId();

        try {
            var updatedRoom = service.addUserToRoom(room, username);
            var users = service.getRoomUsers(room);

            headerAccessor.getSessionAttributes().put("username", username);
            headerAccessor.getSessionAttributes().put("room", room);

            messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + "/roomData", updatedRoom);
            messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + "/users", users);

        } catch (RuntimeException e) {

            System.err.println(e.getMessage());

            messagingTemplate.convertAndSendToUser(sessionId, "/queue/errors", e.getMessage(), createHeaders(sessionId));
        }
    }

    @MessageMapping("/update/{room}")
    public void updateRoom(@DestinationVariable String room, @Payload UpdateRoomDTO data, SimpMessageHeaderAccessor headerAccessor) {
        
        var sessionId = headerAccessor.getSessionId();
        
        try {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
    
            data.setUsername(username);
            data.setRoom(room);
        
            var updatedRoom = service.updateRoomData(data);
            messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + "/roomData", updatedRoom);
            
        } catch (RuntimeException e) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", e.getMessage(), createHeaders(sessionId));
        }
    }
    
    @MessageMapping("/startGame/{room}")
    public void startGame(@DestinationVariable String room, SimpMessageHeaderAccessor headerAccessor) {
        var sessionId = headerAccessor.getSessionId();
        
        try {    
            //TODO: verificar se usuario é owner
            var game = service.createGame(room);  
            
            messagingTemplate.convertAndSend("/topic/" + room + "/game", game);
        
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", e.getMessage(), createHeaders(sessionId));
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        try {
            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            String room = (String) headerAccessor.getSessionAttributes().get("room");

            if(username != null && room != null ){
                var users = service.removeUserFromRoom(room, username);
                messagingTemplate.convertAndSend("/topic/" + room + "/users", users);
            }
            
        } catch (RuntimeException e) {
            System.err.println("ERRO REMOVE USER: " + e.getMessage());
            // messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", e.getMessage(), createHeaders(sessionId));
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
