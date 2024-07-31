package com.ServerManagementTool.server.service;

import com.ServerManagementTool.server.entity.Actions;
import com.ServerManagementTool.server.entity.ServerAction;
import com.ServerManagementTool.server.entity.Status;
import com.ServerManagementTool.server.repository.ServerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Service
public class ServerService {

  private static final Logger logger = LoggerFactory.getLogger(ServerService.class);

  @Autowired
  private ServerRepository serverRepository;

  public List<ServerAction> getAllActions() {
    return serverRepository.findAll();
  }

  public List<ServerAction> getAllActionsByStatus(Status status) {
    return serverRepository.findByActionStatus(status);
  }

  public List<ServerAction> getAllActionsByUserId(Long userId) {
    return serverRepository.findByUserId(userId);
  }

  public ServerAction saveAction(ServerAction action) {
    action.setActionTime(Instant.now());
    return serverRepository.save(action);
  }

  public String performAction(Actions actionName, Long userId) {
    String os = System.getProperty("os.name").toLowerCase();
    String result = "Unknown Action";
    Status status = Status.PENDING;

    try {
      switch (actionName) {
        case SHUTDOWN -> result = shutdown(os);
        case RESTART -> result = restart(os);
        case DELETE_FILE -> result = deleteFile("/home/karan/Karan/Codes/Final Projects/Server-Management-Tool/test.txt");
        case GET_CPU_USAGE -> result = getCpuUsage(os);
        case GET_STORAGE_USAGE -> result = getStorageUsage(os);
        case TERMINATE_ALL_PROCESSES -> {
          ServerAction action = new ServerAction();
          action.setActionName(actionName);
          action.setActionStatus(status);
          action.setUserId(userId);
          action.setActionTime(Instant.now());

          logger.debug("Saving ServerAction: {}", action);

          saveAction(action);
          result = terminateAllProcesses(os);
        }
        default -> throw new IllegalArgumentException("Invalid action: " + actionName);
      }
      status = Status.SUCCESS;
    } catch (IOException | IllegalArgumentException e) {
      logger.error("Error performing action: {}", actionName, e);
      result = "Error: " + e.getMessage();
      status = Status.FAILED;
    }

    ServerAction action = new ServerAction();
    action.setActionName(actionName);
    action.setActionStatus(status);
    action.setUserId(userId);
    action.setActionTime(Instant.now());

    logger.debug("Saving ServerAction: {}", action);

    saveAction(action);

    return result;
  }

  private String shutdown(String os) throws IOException {
    if (os.contains("win")) {
      Runtime.getRuntime().exec("shutdown -s -t 0");
    } else if (os.contains("nix") || os.contains("nux")) {
      Runtime.getRuntime().exec("shutdown -h now");
    }
    return "Shutdown initiated";
  }

  private String restart(String os) throws IOException {
    if (os.contains("win")) {
      Runtime.getRuntime().exec("shutdown -r -t 0");
    } else if (os.contains("nix") || os.contains("nux")) {
      Runtime.getRuntime().exec("reboot");
    }
    return "Restart initiated";
  }

  private String deleteFile(String filePath) throws IOException {
    Files.deleteIfExists(Paths.get(filePath));
    return "File deleted";
  }

  private String getCpuUsage(String os) {
    return os.contains("win") ? getWindowsCpuUsage() : getLinuxCpuUsage();
  }

  private String getStorageUsage(String os) {
    return os.contains("win") ? getWindowsStorageUsage() : getLinuxStorageUsage();
  }

  private String terminateAllProcesses(String os) throws IOException {
    if (os.contains("win")) {
      return terminateAllWindowsProcesses();
    } else if (os.contains("nix") || os.contains("nux")) {
      return terminateAllLinuxProcesses();
    }
    return "Unsupported OS for terminating all processes";
  }

  private String getWindowsCpuUsage() {
    String command = "wmic cpu get loadpercentage";
    try {
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      StringBuilder cpuUsage = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        if (line.matches("\\d+")) {
          cpuUsage.append(line).append("%");
        }
      }
      reader.close();
      return cpuUsage.toString().isEmpty() ? "No CPU usage data available" : "CPU Usage: " + cpuUsage.toString();
    } catch (IOException e) {
      logger.error("Error getting CPU usage on Windows: ", e);
      return "Error retrieving CPU usage data";
    }
  }

  private String getLinuxCpuUsage() {
    try {
      // Read the /proc/stat file which contains CPU usage statistics
      List<String> lines = Files.readAllLines(Paths.get("/proc/stat"));
      for (String line : lines) {
        if (line.startsWith("cpu ")) {
          String[] tokens = line.split("\\s+");
          long userTime = Long.parseLong(tokens[1]);
          long niceTime = Long.parseLong(tokens[2]);
          long systemTime = Long.parseLong(tokens[3]);
          long idleTime = Long.parseLong(tokens[4]);

          // Calculate total CPU time
          long totalTime = userTime + niceTime + systemTime + idleTime;

          // Calculate CPU usage percentage
          double cpuUsage = ((double) (userTime + niceTime + systemTime) / totalTime) * 100;

          return String.format("CPU Usage: %.2f%%", cpuUsage);
        }
      }
    } catch (IOException e) {
      logger.error("Error getting CPU usage on Linux: ", e);
      return "Error retrieving CPU usage data";
    }
    return "No CPU usage data available";
  }

  private String getWindowsStorageUsage() {
    StringBuilder storageUsage = new StringBuilder();
    String command = "wmic logicaldisk get size,freespace,caption";
    try {
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      boolean firstLine = true;
      while ((line = reader.readLine()) != null) {
        if (firstLine) {
          firstLine = false; // Skip the header line
          continue;
        }
        if (line.trim().isEmpty()) continue; // Skip empty lines
        String[] tokens = line.trim().split("\\s+");
        if (tokens.length == 3) {
          String drive = tokens[0];
          long freeSpace = Long.parseLong(tokens[1]);
          long totalSpace = Long.parseLong(tokens[2]);
          long usedSpace = totalSpace - freeSpace;
          double usedSpaceGB = usedSpace / (1024.0 * 1024 * 1024);
          double totalSpaceGB = totalSpace / (1024.0 * 1024 * 1024);
          storageUsage.append(String.format("Drive %s: %.2f GB used of %.2f GB\n", drive, usedSpaceGB, totalSpaceGB));
        }
      }
      reader.close();
    } catch (IOException e) {
      logger.error("Error getting storage usage on Windows: ", e);
      return "Error retrieving storage usage data";
    }
    return storageUsage.toString().isEmpty() ? "No storage usage data available" : storageUsage.toString();
  }

  private String getLinuxStorageUsage() {
    StringBuilder storageUsage = new StringBuilder();
    String command = "df -h";
    try {
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
      String line;
      boolean firstLine = true;
      while ((line = reader.readLine()) != null) {
        if (firstLine) {
          firstLine = false; // Skip the header line
          continue;
        }
        storageUsage.append(line).append("\n");
      }
      reader.close();
    } catch (IOException e) {
      logger.error("Error getting storage usage on Linux: ", e);
      return "Error retrieving storage usage data";
    }
    return storageUsage.toString().isEmpty() ? "No storage usage data available" : storageUsage.toString();
  }

  private String terminateAllWindowsProcesses() throws IOException {
    String command = "taskkill /F /IM *";
    Process process = Runtime.getRuntime().exec(command);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      StringBuilder output = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
      return output.toString().isEmpty() ? "No processes terminated or no output" : output.toString();
    } catch (IOException e) {
      logger.error("Error terminating processes on Windows: ", e);
      throw e;
    }
  }

  private String terminateAllLinuxProcesses() throws IOException {
    String command = "killall -u " + System.getProperty("user.name");
    Process process = Runtime.getRuntime().exec(command);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
      String line;
      StringBuilder output = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
      return output.toString().isEmpty() ? "No processes terminated or no output" : output.toString();
    } catch (IOException e) {
      logger.error("Error terminating processes on Linux: ", e);
      throw e;
    }
  }
}