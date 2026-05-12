package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.entity.User;
import com.fmi_unitbv2026.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/link-uid")
    public ResponseEntity<Void> linkFirebaseUid(@RequestParam String email, @RequestParam String uid) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFirebaseUid(uid);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}