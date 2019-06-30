/*
   --------------------------------------------------------------------------------------------------------------------
   Example sketch/program showing how to read data from a PICC to serial.
   --------------------------------------------------------------------------------------------------------------------
   This is a MFRC522 library example; for further details and other examples see: https://github.com/miguelbalboa/rfid

   Example sketch/program showing how to read data from a PICC (that is: a RFID Tag or Card) using a MFRC522 based RFID
   Reader on the Arduino SPI interface.

   When the Arduino and the MFRC522 module are connected (see the pin layout below), load this sketch into Arduino IDE
   then verify/compile and upload it. To see the output: use Tools, Serial Monitor of the IDE (hit Ctrl+Shft+M). When
   you present a PICC (that is: a RFID Tag or Card) at reading distance of the MFRC522 Reader/PCD, the serial output
   will show the ID/UID, type and any data blocks it can read. Note: you may see "Timeout in communication" messages
   when removing the PICC from reading distance too early.

   If your reader supports it, this sketch/program will read all the PICCs presented (that is: multiple tag reading).
   So if you stack two or more PICCs on top of each other and present them to the reader, it will first output all
   details of the first and then the next PICC. Note that this may take some time as all data blocks are dumped, so
   keep the PICCs at reading distance until complete.

   @license Released into the public domain.

   Typical pin layout used:
   -----------------------------------------------------------------------------------------
               MFRC522      Arduino       Arduino   Arduino    Arduino          Arduino
               Reader/PCD   Uno/101       Mega      Nano v3    Leonardo/Micro   Pro Micro
   Signal      Pin          Pin           Pin       Pin        Pin              Pin
   -----------------------------------------------------------------------------------------
   RST/Reset   RST          9             5         D9         RESET/ICSP-5     RST
   SPI SS      SDA(SS)      10            53        D10        10               10
   SPI MOSI    MOSI         11 / ICSP-4   51        D11        ICSP-4           16
   SPI MISO    MISO         12 / ICSP-1   50        D12        ICSP-1           14
   SPI SCK     SCK          13 / ICSP-3   52        D13        ICSP-3           15
*/

/*
  Thesis Project
  facebook.com/blckclov3r
  instagram.com/blckclov3r
  github.com/blckclov3r
  blckclov3r@gmail.com
  -Aljun Abrenica
*/

#include <SPI.h>
#include <MFRC522.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define RST_PIN D3          // Configurable, see typical pin layout above
#define SS_PIN 2         // Configurable, see typical pin layout above
#define BUZZER D1
#define GPIO16 D0
#define RELAY D2
#define SENSOR D1

String x_data = "";
String  content = "";
boolean wifi_state = false;
byte sensor_state = LOW;
byte val = LOW;

#define FIREBASE_HOST "your firebase host"
#define FIREBASE_AUTH "your firebase auth"
#define WIFI_SSID "your wifi ssid"
#define WIFI_PASSWORD "your wifi password"

MFRC522 mfrc522(SS_PIN, RST_PIN);  // Create MFRC522 instance

void setup() {

  WiFi.setAutoReconnect(true);
  WiFi.setAutoConnect(true);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.begin(9600);
  while (!Serial); // Do nothing if no serial port is opened (added for Arduinos based on ATMEGA32U4)
  SPI.begin();      // Init SPI bus
  mfrc522.PCD_Init();   // Init MFRC522
  mfrc522.PCD_DumpVersionToSerial();  // Show details of PCD - MFRC522 Card Reader details

  pinMode(GPIO16, OUTPUT);
  pinMode(SENSOR, INPUT);
  pinMode(RELAY, OUTPUT);
  digitalWrite(RELAY, HIGH);

  delay(100);
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

}

void loop() {
  if (connectWifi()) {
    if (wifi_state == false) {
      return;
    }
    if (wifi_state != true) {
      wifi_state = false;
    } else {
      for (; wifi_state != false;) {
        digitalWrite(RELAY, HIGH);
        x_data = "";
        content = "";
        val = 0;
        Firebase.setString("state", "X");
        if (Firebase.failed()) {
          wifi_state = false;
          break;
        }
      
        Firebase.remove("state");
        if ( ! mfrc522.PICC_IsNewCardPresent()) {
          return;
        }
        // Select one of the cards
        if ( ! mfrc522.PICC_ReadCardSerial()) {
          return;
        }

        for (byte i = 0; i < mfrc522.uid.size; i++)
        {
         
          content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
          content.concat(String(mfrc522.uid.uidByte[i], HEX));
        }
        Serial.println();
        Serial.print("\tMESSAGE : ");
        content.toUpperCase();
        x_data = Firebase.getString("UNIQUE_ID/" + content.substring(1));

        Serial.println("FIREBASE RFID: " + x_data + " LOCAL RFID: " + content.substring(1));
        String check_lastTimeAccess = "";
        if (content.substring(1) == x_data) {
          //          Firebase.remove("TempData/cardID");
          digitalWrite(RELAY, HIGH);
          Firebase.setString("TempData/cardID", x_data);
          check_lastTimeAccess = Firebase.getString("User/" + x_data + "/lastTimeAccess");
          if ( check_lastTimeAccess == "false") {
            digitalWrite(RELAY, HIGH);
            Serial.println();
            Serial.println("\tResult: " + check_lastTimeAccess);
            check_lastTimeAccess = Firebase.getString("User/" + x_data + "/lastTimeAccess");
            if (check_lastTimeAccess == "true") {
              Serial.println("\tAccess Granted");
              //  Firebase.remove("TempData/cardID");
              //  Firebase.setString("TempData/cardID", x_data);
              digitalWrite(RELAY, LOW);
              val = digitalRead(SENSOR);
              moduleState();
            }
          } else if (check_lastTimeAccess == "true") {
            Serial.println("\tResult: " + check_lastTimeAccess);
            digitalWrite(RELAY, LOW);
            Serial.println("\tAccess Granted");
            //            Firebase.remove("TempData/cardID");
            moduleState();
          } else {
            digitalWrite(RELAY, HIGH);
            Serial.println("check_lastTimeAccess else invoked");
          }
        } else {
          Serial.println("\tAccess Denied!!!");
          Firebase.setString("Unregistered_ID/card_id", content.substring(1));
        }
        //        Firebase.setString("User/" + x_data + "/lastTimeAccess", "false");
        digitalWrite(RELAY, HIGH);
        val = 0;
        x_data = "";
        content = "";
        delay(100);
      }
    } 
  } 
}

void moduleState() {
  val = digitalRead(SENSOR);
  if (val == HIGH) {           // check if the sensor is HIGH
    delay(10);
    Serial.println("\t[1]MOTION DETECTED!");
    if (sensor_state == LOW) {
      delay(10);
      Firebase.setString("SensorData/sensor/" + x_data, "Detected");
      Serial.println("\t[2]Motion detected!, sensor_state == LOW");
      sensor_state = HIGH;       // update variable state to HIGH
      delay(10);
    } else {
      val = digitalRead(SENSOR);
      sensor_state = LOW;
    }
    delay(500);
    val = digitalRead(SENSOR);
    if (val == HIGH) {
      delay(10);
      Serial.println("\t[3]MOTION DETECTED!");
      Firebase.setString("SensorData/sensor/" + x_data, "Detected");
      delay(10);
      if (sensor_state == LOW) {
        delay(10);
        Serial.println("\t[4]Motion detected!, sensor_state == LOW");
        sensor_state = HIGH;       // update variable state to HIGH
      } else {
        val = digitalRead(SENSOR);
        sensor_state = LOW;
      }
    } else {
      delay(10);
      Serial.println("\t[1]MOTION NOT DETECTED!");
      if (sensor_state == HIGH) {
        delay(10);
        Serial.println("\t[2]Motion not detected!, sensor_state == HIGH");
        sensor_state = LOW;       // update variable state to LOW
      } else {
        val = digitalRead(SENSOR);
        sensor_state = HIGH;
      }
    }
  } else {
    Serial.println("\t[2]MOTION NOT DETECTED!");
    if (sensor_state == HIGH) {
      Firebase.remove("SensorData/sensor/" + x_data);
      delay(10);
      Serial.println("\t[3]Motion not detected!, sensor_state == HIGH");
      sensor_state = LOW;       // update variable state to LOW
    } else {
      delay(10);
      val = digitalRead(SENSOR);
      sensor_state = HIGH;
    }
    val = digitalRead(SENSOR);
    delay(100);
    val = digitalRead(SENSOR);
    if (val == HIGH) {
      delay(10);
      Serial.println("\t[5]MOTION DETECTED!");
      Firebase.setString("SensorData/sensor/" + x_data, "Detected");
      if (sensor_state == LOW) {
        delay(10);
        Firebase.setString("SensorData/sensor/" + x_data, "Detected");
        Serial.println("\t[6]Motion detected!, sensor_state == LOW");
        sensor_state = HIGH;       // update variable state to HIGH
      } else {
        val = digitalRead(SENSOR);
        sensor_state = LOW;
      }
    } else {
      delay(10);
      Serial.println("\t[4]MOTION NOT DETECTED!");
      if (sensor_state == HIGH) {
        delay(10);
        Firebase.remove("SensorData/sensor/" + x_data);
        Serial.println("\t[5]Motion not detected!, sensor_state == HIGH");
        sensor_state = LOW;       // update variable state to LOW
      } else {
        sensor_state = HIGH;
      }
    }
  } if (val == HIGH) {          // check if the sensor is HIGH
    delay(10);
    Serial.println("\t[1]MOTION DETECTED!");
    Firebase.setString("SensorData/sensor/" + x_data, "Detected");
    if (sensor_state == LOW) {
      delay(10);
      Firebase.setString("SensorData/sensor/" + x_data, "Detected");
      Serial.println("\t[2]Motion detected!, sensor_state == LOW");
      sensor_state = HIGH;       // update variable state to HIGH
    } else {
      val = digitalRead(SENSOR);
      sensor_state = LOW;
    }
    val = digitalRead(SENSOR);
    delay(100);
    val = digitalRead(SENSOR);
    if (val == HIGH) {
      delay(10);
      Serial.println("\t[3]MOTION DETECTED!");
      Firebase.setString("SensorData/sensor/" + x_data, "Detected");
      if (sensor_state == LOW) {
        delay(10);
        Firebase.setString("SensorData/sensor/" + x_data, "Detected");
        Serial.println("\t[4]Motion detected!, sensor_state == LOW");
        sensor_state = HIGH;       // update variable state to HIGH
      } else {
        val = digitalRead(SENSOR);
        sensor_state = LOW;
      }
    } else {
      delay(10);
      Serial.println("\t[1]MOTION NOT DETECTED!");
      Firebase.remove("SensorData/sensor/" + x_data);
      if (sensor_state == HIGH) {
        delay(10);
        Firebase.remove("SensorData/sensor/" + x_data);
        Serial.println("\t[2]Motion not detected!, sensor_state == HIGH");
        sensor_state = LOW;       // update variable state to LOW
      } else {
        val = digitalRead(SENSOR);
        sensor_state = HIGH;
      }
    }
  } else {
    delay(10);
    Serial.println("\t[2]MOTION NOT DETECTED!");
    Firebase.remove("SensorData/sensor/" + x_data);
    if (sensor_state == HIGH) {
      delay(10);
      Firebase.remove("SensorData/sensor/" + x_data);
      Serial.println("\t[3]Motion not detected!, sensor_state == HIGH");
      sensor_state = LOW;       // update variable state to LOW
    } else {
      val = digitalRead(SENSOR);
      sensor_state = HIGH;
    }
    val = digitalRead(SENSOR);
    delay(100);
    val = digitalRead(SENSOR);
    if (val == HIGH) {
      delay(10);
      Serial.println("\t[5]MOTION DETECTED!");
      Firebase.setString("SensorData/sensor/" + x_data, "Detected");
      if (sensor_state == LOW) {
        delay(10);
        Firebase.setString("SensorData/sensor/" + x_data, "Detected");
        Serial.println("\t[6]Motion detected!, sensor_state == LOW");
        sensor_state = HIGH;       // update variable state to HIGH
      } else {
        val = digitalRead(SENSOR);
        sensor_state = LOW;
      }
    } else {
      delay(10);
      Serial.println("\t[4]MOTION NOT DETECTED!");
      Firebase.remove("SensorData/sensor/" + x_data);
      if (sensor_state == HIGH) {
        delay(10);
        Firebase.remove("SensorData/sensor/" + x_data);
        Serial.println("\t[5]Motion not detected!, sensor_state == HIGH");
        sensor_state = LOW;       // update variable state to LOW
      } else {
        sensor_state = HIGH;
      }
    }
  }

  delay(5000);
  digitalWrite(RELAY, HIGH);
  Serial.println("\tDONE");
}

boolean connectWifi() {

  if (!wifi_state) {
    int i = 0;
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.println("");
    Serial.println("Connecting to WiFi");

    // Wait for connection
    Serial.print("Connecting ...");
    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(".");
      if (i > 10) {
        wifi_state = false;
        break;
      }
      i++;
    }
    if (!wifi_state) {
      Serial.println("");
      Serial.print("Connected to ");
      Serial.println(WIFI_SSID);
      Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
      wifi_state = true;
    }
    else {
      Serial.println("");
      Serial.println("Connection failed.");
      wifi_state = false;
    }
  } else {
    wifi_state = true;
  }
  return wifi_state;
}
