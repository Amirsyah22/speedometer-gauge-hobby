/*
  ESP32 C3 SuperMini - Proximity Speedometer

  Kondisi yang dipakai:
  - 1 magnet pada roda depan
  - roda depan keliling = 1.752 meter
  - sensor proximity mendeteksi magnet
  - data dikirim via USB serial (Arduino IDE / ESP32 serial monitor)

  Output format:
  SPEED:xx.xx|RPM:xx.xx

  Untuk Android via USB OTG, aplikasi Java dapat membaca serial data
  dengan baud rate 115200.
*/

#include <Arduino.h>

const uint8_t PROX_PIN = 2;                  // Ganti sesuai pin sensor Anda
const uint8_t PULSES_PER_REV = 1;            // 1 magnet pada roda depan
const float WHEEL_CIRCUMFERENCE_M = 1.752f; // Keliling roda depan (meter)
const uint32_t REPORT_INTERVAL_MS = 200;    // Kirim data tiap 200 ms
const uint32_t PULSE_DEBOUNCE_US = 5000;   // Debounce pulsa

volatile uint32_t pulseCount = 0;
volatile uint32_t lastPulseMicros = 0;
uint32_t lastReportMs = 0;

void IRAM_ATTR onPulse() {
  uint32_t now = micros();

  if (lastPulseMicros == 0 || (now - lastPulseMicros) > PULSE_DEBOUNCE_US) {
    pulseCount++;
    lastPulseMicros = now;
  }
}

void setup() {
  pinMode(PROX_PIN, INPUT_PULLUP);

  // Banyak sensor proximity aktif LOW saat mendeteksi benda.
  // Jika sensor Anda aktif HIGH, ganti FALLING menjadi RISING.
  attachInterrupt(digitalPinToInterrupt(PROX_PIN), onPulse, FALLING);

  Serial.begin(115200);
  delay(500);

  Serial.println("ESP32 Proximity Speedometer Ready");
  Serial.println("Format: SPEED:xx.xx|RPM:xx.xx");
}

void loop() {
  uint32_t now = millis();

  if (now - lastReportMs >= REPORT_INTERVAL_MS) {
    lastReportMs = now;

    static uint32_t lastPulseSnapshot = 0;
    uint32_t deltaPulses = pulseCount - lastPulseSnapshot;
    lastPulseSnapshot = pulseCount;

    float elapsedSeconds = REPORT_INTERVAL_MS / 1000.0f;
    float pulsePerSecond = deltaPulses / elapsedSeconds;

    // Kecepatan km/h:
    // speed = (pulsePerSecond * kelilingRoda * 3.6) / pulsesPerRev
    float speedKmh = (pulsePerSecond * WHEEL_CIRCUMFERENCE_M * 3.6f) / PULSES_PER_REV;

    // RPM:
    // rpm = (pulsePerSecond * 60) / pulsesPerRev
    float rpm = (pulsePerSecond * 60.0f) / PULSES_PER_REV;

    if (speedKmh < 0) speedKmh = 0;
    if (rpm < 0) rpm = 0;

    Serial.print("SPEED:");
    Serial.print(speedKmh, 2);
    Serial.print("|RPM:");
    Serial.println(rpm, 2);
  }
}
