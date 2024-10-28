package dev.pedrofaleiros.whoiswho_api.controller;

import java.security.Principal;
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
    public void joinRoom(@DestinationVariable String room, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        var sessionId = headerAccessor.getSessionId();
        var username = service.extractUsername(principal);

        // long t1;
        // t1 = System.nanoTime();
        var updatedUsers = service.addUserToRoom(room, username, sessionId);
        // System.out.println("Add User: " + ( (System.nanoTime() - t1) /1_000_000.0));
        
        // t1 = System.nanoTime();
        var roomData = service.getRoomData(room);
        // System.out.println("Room Data: " + ( (System.nanoTime() - t1) /1_000_000.0));
        
        headerAccessor.getSessionAttributes().put("room", room);
        
        // t1 = System.nanoTime();
        var game = service.getLatestGame(room);
        // System.out.println("Latest Game: " + ( (System.nanoTime() - t1) /1_000_000.0));
        if(game != null){
            messagingTemplate.convertAndSendToUser(sessionId, "queue/gameData", game, createHeaders(sessionId));
        }
        
        // t1 = System.nanoTime();
        var games = service.listRoomGames(room);
        // System.out.println("List Games: " + ( (System.nanoTime() - t1) /1_000_000.0));
        
        // System.out.println("Concluido!");

        messagingTemplate.convertAndSendToUser(sessionId, "queue/gamesList", games, createHeaders(sessionId));

        messagingTemplate.convertAndSendToUser(sessionId, "queue/roomData", roomData, createHeaders(sessionId));
        messagingTemplate.convertAndSend("/topic/" + roomData.getId() + ".users", updatedUsers);
    }

    @MessageMapping("/update/{room}")
    public void updateRoom(@DestinationVariable String room, @Payload UpdateRoomDTO data, Principal principal) {
        var username = service.extractUsername(principal);

        data.setUsername(username);
        data.setRoom(room);
    
        var updatedRoom = service.updateRoomData(data);
        messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + ".roomData", updatedRoom);
    }
    
    @MessageMapping("/startGame/{room}")
    public void startGame(@DestinationVariable String room, Principal principal) {
        var username = service.extractUsername(principal);
        
        var game = service.startGame(room, username);
        var updatedRoom = service.getRoomData(room);

        countdown(room);
        
        messagingTemplate.convertAndSend("/topic/" + game.getRoom().getId() + ".gameData", game);
        messagingTemplate.convertAndSend("/topic/" + game.getRoom().getId() + ".roomData", updatedRoom);
        
        messagingTemplate.convertAndSend("/topic/" + room + ".countdown", 0);
    }
    
    @MessageMapping("/finishGame/{room}")
    public void finishGame(@DestinationVariable String room, Principal principal) {
        var username = service.extractUsername(principal);
        
        var updatedRoom = service.finishGame(room, username);
        var games = service.listRoomGames(room);
        
        messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + ".roomData", updatedRoom);
        messagingTemplate.convertAndSend("/topic/" + updatedRoom.getId() + ".gamesList", games);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        try {
            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
            
            String room = (String) headerAccessor.getSessionAttributes().get("room");
            String sessionId = headerAccessor.getSessionId();

            if(sessionId != null && room != null ){
                var users = service.removeUserFromRoom(sessionId, room);
                messagingTemplate.convertAndSend("/topic/" + room + ".users", users);
            }
            
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void countdown(String room) {
        try {
            messagingTemplate.convertAndSend("/topic/" + room + ".countdown", 3);
            Thread.sleep(1000);
            messagingTemplate.convertAndSend("/topic/" + room + ".countdown", 2);
            Thread.sleep(1000);
            messagingTemplate.convertAndSend("/topic/" + room + ".countdown", 1);
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
