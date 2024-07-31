import 'package:flutter/material.dart';

class CustomCard extends StatelessWidget {
  final double width;
  final double height;
  final Widget child;
  final EdgeInsetsGeometry margin;

  const CustomCard({
    super.key,
    required this.width,
    required this.height,
    required this.child,
    this.margin = const EdgeInsets.all(8.0),
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      margin: margin,
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2), // Shadow color
            spreadRadius: 1, // Spread radius
            blurRadius: 5, // Blur radius
            offset: const Offset(0, 3), // Offset in the x and y directions
          ),
        ],
      ),
      child: Center(child: child),
    );
  }
}
