package com.fmi_unitbv2026.demo.entity;
import com.fmi_unitbv2026.demo.enums.Role;

import jakarta.persistence.*;

@Entity
@Table(name = "login_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int idUser;

    @Column(name = "email")
    private String email;

    @Column(name = "firebase_uid")
    private String firebaseUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User() { }
    public User(String email, String firebaseUid, Role role) {
        this.email = email;
        this.firebaseUid = firebaseUid;
        this.role = role;
    }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public String getFirebaseUid() { return firebaseUid; }

    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getIdUser() { return idUser; }

    public void setIdUser(int idUser) { this.idUser = idUser; }
}
