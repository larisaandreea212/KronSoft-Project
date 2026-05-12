package com.fmi_unitbv2026.demo.services;

import com.fmi_unitbv2026.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fmi_unitbv2026.demo.entity.User;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByFirebaseUid(String uid) {
        return userRepository.findByFirebaseUid(uid);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateFirebaseUid(String email, String uid) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        user.setFirebaseUid(uid);
        userRepository.save(user);
    }
}