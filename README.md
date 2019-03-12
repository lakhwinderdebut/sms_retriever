# sms_retriever

A new Flutter plugin to retrieve the SMS on Android using SMS Retrieval API

## Getting Started

To retrieve a app signature. It requires by the SMS
```dart
String appSignature = await SmsRetriever.getAppSignature();
```
To start listening for an incoming SMS
```dart
String smsCode = await SmsRetriever.startListening();
```
Stop listening after getting the SMS
```dart
SmsRetriever.stopListening();
```

Generate appSignature for keystore file
````dart in html
keytool -storepass storepass -alias alias -exportcert -keystore file | xxd -p | tr -d "[:space:]" | xxd -r -p | base64 | cut -c1-11

````

Example SMS

[#] Your example code is:
123456
appSignature