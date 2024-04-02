import 'package:flutter/services.dart';

class NativeServiceHelper {
  static const MethodChannel _channel = MethodChannel('com.example.timer/Timer');

  static Future<void> startForegroundService() async {
    try {
      await _channel.invokeMethod('startForegroundService');
    } on PlatformException catch (e) {
      print("Failed to start the service: '${e.message}'.");
    }
  }
  static Future<void> startTimerActivity() async {
    try {
      await _channel.invokeMethod('startTimerActivity');
    } on PlatformException catch (e) {
      print("Failed to start the service: '${e.message}'.");
    }
  }
}