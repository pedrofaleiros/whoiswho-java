package dev.pedrofaleiros.whoiswho_api.controller;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class TesteController {
    
    @GetMapping("teste")
    public ResponseEntity<String> teste(Principal principal) {
        return ResponseEntity.ok().body(String.format("Hello, %s!", principal.getName()));
    }
    
    @GetMapping("testando/denovo")
    public ResponseEntity<String> teste2(Principal principal) {
        return ResponseEntity.ok().body(String.format("Hello, %s!", principal.getName()));
    }
}
