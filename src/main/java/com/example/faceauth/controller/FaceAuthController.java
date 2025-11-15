package com.example.faceauth.controller;

import com.example.faceauth.service.FaceAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/face")
@CrossOrigin(origins = "*")
public class FaceAuthController {

    @Autowired
    private FaceAuthService service;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam("userId") String userId,
            @RequestParam("image") MultipartFile image) {
        Map<String, Object> res = new HashMap<>();
        try {
            double[] emb = service.getEmbeddingFromPython(image.getBytes());
            service.saveEmbedding(userId, emb);
            res.put("success", true);
            res.put("message", "Registered: " + userId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(
            @RequestParam("userId") String userId,
            @RequestParam("image") MultipartFile image) {
        Map<String, Object> res = new HashMap<>();
        try {
            double[] emb = service.getEmbeddingFromPython(image.getBytes());
            boolean match = service.verifyUser(userId, emb);
            res.put("success", match);
            res.put("message", match ? "Login Success!" : "Failed!");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }
}