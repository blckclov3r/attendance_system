# Attendance System 
* An android application for student attendance
* Android + Arduino ESP8266 12e + Firebase Realtime Database

## Screenshots
<p align="center"> 
<img src="https://user-images.githubusercontent.com/43292234/60387201-989f0700-9a54-11e9-8163-1fb957260b72.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387202-989f0700-9a54-11e9-912e-9c1984bb7805.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387203-99379d80-9a54-11e9-8b09-d61fb099b3ec.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387204-99379d80-9a54-11e9-9234-02074f2fdf76.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387205-99379d80-9a54-11e9-9a9f-b14a16fdc2a5.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387206-99d03400-9a54-11e9-987e-1fb5a2b5bd4f.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387208-9b016100-9a54-11e9-8b5c-33c63c97e385.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387209-9b016100-9a54-11e9-9816-351a7eb7193c.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387210-9b99f780-9a54-11e9-92fe-d456b20608f7.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387211-9ccb2480-9a54-11e9-8b00-a2d343c73e68.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387212-9d63bb00-9a54-11e9-9fbd-067d25e0103b.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387213-9d63bb00-9a54-11e9-93cd-79473bd65df1.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387214-9dfc5180-9a54-11e9-877c-5ff84d3ad666.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387215-9e94e800-9a54-11e9-8630-e35381ce6f41.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387216-9f2d7e80-9a54-11e9-8c79-4c8dee421795.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387218-9fc61500-9a54-11e9-8352-812fb4bb0a98.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387219-a05eab80-9a54-11e9-83ae-296286e9357c.png" width="130" height="190">
<img src="https://user-images.githubusercontent.com/43292234/60387220-a05eab80-9a54-11e9-86fd-761689a5378c.png" width="130" height="190">
</p>

## Feature
* Realtime Access (Online).
* Auto-generated attendance and logs.
* Can't enter when the subject has not started.
* 30 minutes passed, auto generated as late .
* 1 hr. or greater are unable to enter.
* Login Support for authorized user.
* ETC.

## Security
* The UID of a card can not be used as an unique identification for security related projects.
Some cards allow to change the UID which means you can easily clone a card. For projects like access control,
door opener or payment systems you must implement an additional security mechanism like a password or normal key.

## Hardware
* MFRC522 RFID
  * Read and write different types of Radio-Frequency IDentification (RFID) cards on your 
  Arduino using a RC522 based reader connected via the Serial Peripheral Interface (SPI) interface.
* HC-SR501
  * PIR motion sensor works by constantly comparing the level of infrared radiation (IR) of the surrounding are, this allows the sensor to detect motion,
  these sensors are commonly used for home automation, such as automated lighting systems, and security systems.
* NodeMCU 12e
  *  WiFi development board that helps you to prototype your IoT project.
* Relay
  * is an electrically operated switch. Many relays use an electromagnet to mechanically operate a switch, but other operating principles are also used, such as solid-state relays. Relays are used where it is necessary to control a circuit by a separate low-power signal, 
  or where several circuits must be controlled by one signal.
* Solinoid Doorlock 12v
  * This works on the principle of electromagnetism, wherein a DC current creates a magnetic field which moves the slug.
* 12v Adapter

## Software
* Android Studio v3.3
* Arduino v1.8.8

## Backend
 * Firebase Realtime Database

## Dependencies
 * implementation 'com.android.support:appcompat-v7:28.0.0'
 * implementation 'com.android.support:design:28.0.0'
 * implementation 'com.android.support:support-v4:28.0.0'
 * implementation 'com.android.support:recyclerview-v7:28.0.0'
 * implementation 'com.android.support:cardview-v7:28.0.0'
 * implementation 'com.android.support.constraint:constraint-layout:1.1.3'
 * implementation 'com.google.firebase:firebase-core:16.0.7'
 * implementation 'com.google.firebase:firebase-database:16.0.6'
 * implementation 'com.google.firebase:firebase-storage:15.0.0'
 * implementation 'com.google.firebase:firebase-auth:16.1.0'
 * implementation 'com.squareup:android-times-square:1.6.5@aar'
 * implementation 'com.squareup.picasso:picasso:2.71828'
 * implementation 'de.hdodenhof:circleimageview:3.0.0'
 * implementation 'com.github.f0ris.sweetalert:library:1.5.6'
 * implementation 'com.github.GrenderG:Toasty:1.3.1'
 * implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0-alpha'
 * implementation "com.google.firebase:firebase-measurement-connector-impl:17.0.5"
 * implementation 'hari.bounceview:bounceview:0.1.0'
 * implementation 'com.fujiyuu75:sequent:0.2.1'
 * implementation 'com.github.Binary-Finery:Bungee:master-SNAPSHOT'
 * implementation 'com.github.chivorns:smartmaterialspinner:1.1.0'
