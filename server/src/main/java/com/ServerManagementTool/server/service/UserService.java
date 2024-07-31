package com.ServerManagementTool.server.service;

import com.ServerManagementTool.server.entity.User;
import com.ServerManagementTool.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(Long userId) {
    return userRepository.findByUserId(userId);
  }

  public User addUser(User user) {
    return userRepository.save(user);
  }

  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
  }

  public boolean authenticate(String email, String password) {
    Optional<User> user = userRepository.findByUserEmail(email);
    return user.isPresent() && user.get().getUserPass().equals(password);
  }
}
