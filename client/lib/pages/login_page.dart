import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:client/pages/home_page.dart';
import 'package:client/components/my_button.dart';
import 'package:client/components/my_textfield.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  // Text editing controllers
  final usernameController = TextEditingController();
  final passwordController = TextEditingController();

  // Method to handle login
  Future<void> login() async {
    const url = 'http://192.168.0.248:8080/api/users/login';

    try {
      final response = await http.post(
        Uri.parse(url),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'email': usernameController.text,
          'password': passwordController.text,
        }),
      );

      if (response.statusCode == 200) {
        // Navigate to the HomePage upon successful login
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => const HomePage()),
        );
        print('Login successful');
      } else {
        print('Failed to login: ${response.statusCode}');
        // Optionally handle the error state here, e.g., showing a dialog
      }
    } catch (e) {
      print('Error during login: $e');
      // Optionally handle the error state here, e.g., showing a dialog
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[300],
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const SizedBox(height: 50),

                  // Logo
                  const Icon(
                    Icons.lock,
                    size: 100,
                  ),

                  const SizedBox(height: 50),

                  // Welcome message
                  Text(
                    'Welcome back, you\'ve been missed!',
                    style: TextStyle(
                      color: Colors.grey[700],
                      fontSize: 16,
                    ),
                  ),

                  const SizedBox(height: 25),

                  // Username textfield
                  MyTextField(
                    controller: usernameController,
                    hintText: 'Username',
                    obscureText: false,
                  ),

                  const SizedBox(height: 10),

                  // Password textfield
                  MyTextField(
                    controller: passwordController,
                    hintText: 'Password',
                    obscureText: true,
                  ),

                  const SizedBox(height: 10),

                  // Forgot password?
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 25.0),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        Text(
                          'Forgot Password?',
                          style: TextStyle(color: Colors.grey[600]),
                        ),
                      ],
                    ),
                  ),

                  const SizedBox(height: 25),

                  // Sign in button
                  MyButton(
                    onTap: login,
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
