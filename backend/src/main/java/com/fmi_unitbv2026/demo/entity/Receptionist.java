package com.fmi_unitbv2026.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "receptionist")
public class Receptionist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receptionist")
    private int idReceptionist;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public Receptionist() {}

    public int getIdReceptionist() { return idReceptionist; }
    public void setIdReceptionist(int idReceptionist) { this.idReceptionist = idReceptionist; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}