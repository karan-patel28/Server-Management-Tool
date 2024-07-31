package com.ServerManagementTool.server.controller;

import com.ServerManagementTool.server.entity.User;
import com.ServerManagementTool.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUser();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable Long userId) {
    Optional<User> user = userService.getUserById(userId);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/register")
  public ResponseEntity<User> addUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.addUser(user));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody UserLoginRequest request, HttpServletRequest httpServletRequest) {
    boolean isAuthenticated = userService.authenticate(request.getEmail(), request.getPassword());
    if (isAuthenticated) {
      // Set session attribute
      httpServletRequest.getSession().setAttribute("user", request.getEmail());
      return ResponseEntity.ok("Login successful");
    } else {
      return ResponseEntity.status(401).body("Invalid email or password");
    }
  }


  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();  // Invalidate the session if it exists
    }
    return ResponseEntity.ok("Logout successful");
  }

  @Data
  public static class UserLoginRequest {
    private String email;
    private String password;
  }
}
