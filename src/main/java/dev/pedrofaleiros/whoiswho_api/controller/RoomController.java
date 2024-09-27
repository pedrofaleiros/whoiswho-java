package dev.pedrofaleiros.whoiswho_api.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


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

}
