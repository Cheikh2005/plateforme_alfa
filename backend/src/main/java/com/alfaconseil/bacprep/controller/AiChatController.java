package com.alfaconseil.bacprep.controller;

import com.alfaconseil.bacprep.dto.ChatRequest;
import com.alfaconseil.bacprep.model.ChatMessage;
import com.alfaconseil.bacprep.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChatRequest request) {
        try {
            String reponse = aiChatService.chat(
                    userDetails.getUsername(),
                    request.getMessage(),
                    request.getMatiereId());
            return ResponseEntity.ok(Map.of("reponse", reponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(aiChatService.getHistory(userDetails.getUsername()));
    }

    @DeleteMapping("/history")
    public ResponseEntity<?> clearHistory(@AuthenticationPrincipal UserDetails userDetails) {
        aiChatService.clearHistory(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Historique effacé"));
    }
}