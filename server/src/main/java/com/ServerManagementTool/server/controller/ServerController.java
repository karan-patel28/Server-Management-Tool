package com.ServerManagementTool.server.controller;

import com.ServerManagementTool.server.entity.Actions;
import com.ServerManagementTool.server.entity.ServerAction;
import com.ServerManagementTool.server.entity.Status;
import com.ServerManagementTool.server.entity.User;
import com.ServerManagementTool.server.service.ServerService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/server-actions")
public class ServerController {

  @Autowired
  private ServerService serverActionsService;

  @GetMapping
  public ResponseEntity<List<ServerAction>> getAllActions() {
    List<ServerAction> actions = serverActionsService.getAllActions();
    return ResponseEntity.ok(actions);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<ServerAction>> getActionsByStatus(@PathVariable Status status) {
    List<ServerAction> actions = serverActionsService.getAllActionsByStatus(status);
    return ResponseEntity.ok(actions);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ServerAction>> getActionsByUser(@PathVariable Long userId) {
    User user = new User();
    user.setUserId(userId);
    List<ServerAction> actions = serverActionsService.getAllActionsByUserId(userId);
    return ResponseEntity.ok(actions);
  }

  @PostMapping("/perform")
  public ResponseEntity<String> performAction(@RequestBody PerformActionRequest request) {
    String result = serverActionsService.performAction(request.getAction(), request.user.getUserId());
    return ResponseEntity.ok(result);
  }

  @Getter
  @Setter
  public static class PerformActionRequest {
    private Actions action;
    private User user;
  }
}
