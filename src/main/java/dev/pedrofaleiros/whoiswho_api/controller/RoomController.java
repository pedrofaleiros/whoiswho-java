package dev.pedrofaleiros.whoiswho_api.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("room")
public class RoomController {

    @Autowired
    private RoomService service;

    @PostMapping
    public ResponseEntity<String> postMethodName(Principal principal) {
        var data = service.createRoom(principal.getName());
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("{roomId}")
    public ResponseEntity<Room> getMethodName(@PathVariable String roomId) {
        var data = service.findById(roomId);
        return ResponseEntity.ok().body(data);
    }


}
