package com.innovx.gestionrh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstName")
   private String firstName;
    @Column(name = "lastName")
   private String lastName;
    @Column(name ="email", unique = true)
    private String email;
    @Column(name = "title")
    private String title;
    @Column(name = "password")
    private String password;
    @Column(name = "lastPasswordChange")
    private LocalDateTime lastPasswordChange;
    @Column(name = "userRole")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Builder.Default
    private boolean isDeleted= false;

}
