# Server Management Tool

A mobile application for managing server operations such as shutting down, restarting, monitoring CPU and storage usage, and more. Built using Flutter, this app interfaces with a Spring Boot backend to perform various server management tasks.

## Features

- **Login Authentication**: Secure login for authorized users.
- **Server Control**: Options to shutdown, restart, delete files, and terminate processes.
- **Monitoring**: Real-time monitoring of CPU usage, storage usage, and system temperature.
- **Cross-Platform**: Built with Flutter, the app runs on both Android and iOS.

<!-- ## Screenshots

_Add screenshots or GIFs of your app here_ -->

## Installation

### Backend Setup (Spring Boot)

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/server-management-tool.git
   cd server-management-tool
   ```

2. **Database Setup**:
   - Install PostgreSQL if not already installed.
   - Create a new PostgreSQL database.
   - Run the SQL script to set up the tables and initial data:
   ```bash
   psql -U your_username -d your_database -a -f db.sql
   ```
   - Update the application.properties file in the Spring Boot project with your database credentials:
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the backend server:**:
   - Navigate to the Spring Boot project directory.
   - Build and run the server:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### **Frontend Setup (Flutter)**

1. **Install Flutter dependencies**:
   - Ensure that you have Flutter installed. Then run:
   ```bash
   flutter pub get
   ```

2. **Configure API URL**:
   - Update the API base URL in your Flutter app to point to the backend server.

3. **Run the app**:
   - Connect your mobile device or start an emulator.
   - Run the app with:
   ```bash
   flutter run
   ```

## Usage
1. **Login**: Use your credentials to log into the app.
2. **Dashboard**: Access various server management options.
3. **Monitor**: View real-time data on CPU usage and storage.
4. **Control**: Execute server commands like shutdown, restart, and more.