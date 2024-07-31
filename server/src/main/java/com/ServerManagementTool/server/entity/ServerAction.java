package com.ServerManagementTool.server.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "server_actions")
public class ServerAction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long actionId;

  @Enumerated(EnumType.STRING)
  private Actions actionName;

  @Enumerated(EnumType.STRING)
  private Status actionStatus;

  @Column(name = "timestamp")
  private Instant actionTime;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @PrePersist
  public void createAction() {
    this.actionTime = Instant.now();
  }
}
