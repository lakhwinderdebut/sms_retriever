# sms_retriever

A new Flutter plugin to retrieve the SMS on Android using SMS Retrieval API

## Getting Started

To retrieve a app signature. It requires by the SMS

```dart
String appSignature = await SmsRetriever.getAppSignature();


String smsCode = await SmsRetriever.startListening();

SmsRetriever.stopListening();
```