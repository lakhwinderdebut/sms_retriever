# sms_retriever

A new Flutter plugin to retrieve the SMS on Android using SMS Retrieval API

## Getting Started

To retrieve a app signature. It requires by the SMS
```dart
String appSignature = await SmsRetriever.getAppSignature();
```
To stat listening for and incoming SMS
```dart
String smsCode = await SmsRetriever.startListening();
```
Stop listening after getting the SMS
```dart
SmsRetriever.stopListening();
```


Example SMS

[#] Your example code is:
123456
appSignature