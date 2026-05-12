package com.fmi_unitbv2026.demo.controller;

import com.fmi_unitbv2026.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fmi_unitbv2026.demo.entity.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/{uid}")
    public ResponseEntity<User> getUserByFirebaseUid(@PathVariable String uid) {
        return userService.findByFirebaseUid(uid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
