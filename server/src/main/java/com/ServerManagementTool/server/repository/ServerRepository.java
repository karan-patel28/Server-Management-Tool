package com.ServerManagementTool.server.repository;

import com.ServerManagementTool.server.entity.ServerAction;
import com.ServerManagementTool.server.entity.Status;
import com.ServerManagementTool.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<ServerAction, Long> {
  List<ServerAction> findByActionStatus(Status status);
  List<ServerAction> findByUserId(Long userId);
}
