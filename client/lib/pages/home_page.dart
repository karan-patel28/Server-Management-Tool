import 'dart:async'; // Import Timer class
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:client/components/my_custom_button.dart'; // Import CustomButton widget
import 'package:client/components/my_card.dart'; // Import CustomCard widget
import 'package:client/pages/login_page.dart'; // Import the LoginPage widget

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String cpuUsage = 'Fetching CPU usage...';
  String storageUsage = 'Fetching storage usage...';
  Timer? _cpuTimer;
  Timer? _storageTimer;

  @override
  void initState() {
    super.initState();
    _startCpuUsageTimer();
    _startStorageUsageTimer();
  }

  void _startCpuUsageTimer() {
    _cpuTimer = Timer.periodic(Duration(seconds: 5), (timer) {
      fetchData('GET_CPU_USAGE', (data) {
        setState(() {
          cpuUsage = data;
        });
      });
    });
  }

  void _startStorageUsageTimer() {
    _storageTimer = Timer.periodic(Duration(seconds: 5), (timer) {
      fetchData('GET_STORAGE_USAGE', (data) {
        setState(() {
          storageUsage = data;
        });
      });
    });
  }

  Future<void> fetchData(String action, Function(String) onSuccess) async {
    const url = 'http://192.168.0.248:8080/api/server-actions/perform';
    const userId = '1';

    try {
      final response = await http.post(
        Uri.parse(url),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'action': action,
          'user': {'userId': userId},
        }),
      );

      if (response.statusCode == 200) {
        onSuccess(response.body);
        print('$action fetched successfully');
      } else {
        print('Failed to fetch $action: ${response.statusCode}');
      }
    } catch (e) {
      print('Error fetching $action: $e');
    }
  }

  void refreshData() {
    fetchData('GET_CPU_USAGE', (data) {
      setState(() {
        cpuUsage = data;
      });
    });
    fetchData('GET_STORAGE_USAGE', (data) {
      setState(() {
        storageUsage = data;
      });
    });
  }

  @override
  void dispose() {
    _cpuTimer?.cancel();
    _storageTimer?.cancel();
    super.dispose();
  }

  Future<void> performServerAction(String action) async {
    const url = 'http://192.168.0.248:8080/api/server-actions/perform';
    const userId = '1';

    try {
      final response = await http.post(
        Uri.parse(url),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'action': action,
          'user': {'userId': userId},
        }),
      );

      if (response.statusCode == 200) {
        print('Action performed successfully: ${response.body}');
      } else {
        print('Failed to perform action: ${response.statusCode}');
      }
    } catch (e) {
      print('Error: $e');
    }
  }

  Future<void> logout() async {
    const url = 'http://192.168.0.248:8080/api/users/logout';

    try {
      final response = await http.post(
        Uri.parse(url),
        headers: {'Content-Type': 'application/json'},
      );

      if (response.statusCode == 200) {
        print('Logout successful');
        Navigator.pushAndRemoveUntil(
          context,
          MaterialPageRoute(builder: (context) => const LoginPage()),
          (route) => false,
        );
      } else {
        print('Failed to logout: ${response.statusCode}');
        // Handle logout failure appropriately
      }
    } catch (e) {
      print('Error during logout: $e');
      // Handle error appropriately
    }
  }

  void showLogoutConfirmationDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text("Confirm Logout"),
          content: const Text("Are you sure you want to log out?"),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); // Close the dialog
              },
              child: const Text("Cancel"),
            ),
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); // Close the dialog
                logout(); // Proceed with logout
              },
              child: const Text("Logout"),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard'),
        backgroundColor: Colors.black,
        titleTextStyle: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
          fontSize: 20,
        ),
        iconTheme: const IconThemeData(
          color: Colors.white,
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () {
              print('Refresh icon pressed');
              refreshData();
            },
          ),
          IconButton(
            onPressed: () {
              showLogoutConfirmationDialog();
            },
            icon: const Icon(Icons.logout),
          )
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(10),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            Row(
              children: [
                Expanded(
                  child: CustomButton(
                    text: 'Shutdown',
                    onTap: () {
                      print('Shutdown pressed');
                      performServerAction('SHUTDOWN');
                    },
                  ),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: CustomButton(
                    text: 'Restart',
                    onTap: () {
                      print('Restart pressed');
                      performServerAction('RESTART');
                    },
                  ),
                ),
              ],
            ),
            const SizedBox(height: 10),
            CustomCard(
              width: double.infinity,
              height: 50,
              child: Text('CPU Usage: $cpuUsage'),
            ),
            const SizedBox(height: 10),
            CustomCard(
              width: double.infinity,
              height: 260,
              child: Text('Storage Usage:\n$storageUsage'),
            ),
            const SizedBox(height: 10),
            Row(
              children: [
                Expanded(
                  child: CustomButton(
                    text: 'Delete File',
                    onTap: () {
                      print('Delete pressed');
                      performServerAction('DELETE_FILE');
                    },
                  ),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: CustomButton(
                    text: 'Terminate Process',
                    onTap: () {
                      print('Terminate pressed');
                      performServerAction('TERMINATE_ALL_PROCESSES');
                    },
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
