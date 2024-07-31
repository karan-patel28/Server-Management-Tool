package com.ServerManagementTool.server.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(name = "username", nullable = false, length = 50)
  private String userName;

  @Column(name = "password", nullable = false, length = 100)
  private String userPass;

  @Column(name = "email", nullable = false, length = 100)
  private String userEmail;

  @Column(name = "created_at")
  private Instant createAt;

  @PrePersist
  public void creteDate() {
    this.createAt = Instant.now();
  }

}
