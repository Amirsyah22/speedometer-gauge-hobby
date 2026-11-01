# Speedometer Hobby App

This app is for proximity-based speedometer, not GPS.

This project is an Android hobby project for a custom speedometer view driven by an ESP32 C3 SuperMini using USB serial communication to an Android Marshmallow device. It is not a production-ready application and is intended for personal experimentation and learning.

## Description
This project was created as a hobby experiment using AI assistance. It is not a professional app and it has not been tested in a production environment. It is intended only as a personal learning project and a place to store experimental code fragments.

The idea is to use a proximity sensor connected to an ESP32 C3 SuperMini, then send calculated speed data over USB serial to an Android Marshmallow phone. Since the device is old, the app is kept simple and focused on speed display instead of GPS-based tracking.

## Important notes
- This app is for proximity-based speedometer, not GPS.
- Not production-ready, hobby project.
- ESP32 C3 SuperMini + USB to Android Marshmallow.
- The USB connection is used as the communication path between the sensor board and the phone.
- If the USB disconnects, Tasker can be used to trigger a shutdown or power-off action on the device, depending on your setup.
- This project is not intended for professional or commercial use.
- The code may contain bugs, rough edges, or incomplete parts.

## Features
- Neon cyan dashboard gauge
- Speed display like a car instrument cluster
- Speed limit badge
- Gear indicator row
- Java-friendly Android app structure for older device compatibility
- USB serial data-handler concept for sensor input

## Run this project
1. Open in Android Studio.
2. Let Gradle sync.
3. Run the app on an emulator or device.
4. Connect the ESP32 C3 SuperMini via USB.
5. Feed speed data from the sensor system to the app.

## Hardware concept
- Sensor: proximity sensor
- Controller: ESP32 C3 SuperMini
- Communication: USB serial
- Receiver: Android Marshmallow device
- Use case: custom speedometer display for hobby or experimental use

## Disclaimer
This app is intentionally simple and designed for learning, creativity, and personal experimentation. It should not be treated as a production-ready application.
