package dev.pedrofaleiros.whoiswho_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("Hello World!");
    }
}
