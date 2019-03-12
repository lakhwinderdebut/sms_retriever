import 'package:flutter/material.dart';
import 'package:sms_retriever/sms_retriever.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _smsCode = "";
  bool isListening = false;

  getCode(String sms) {
    if (sms != null) {
      final intRegex = RegExp(r'\d+', multiLine: true);
      final code = intRegex.allMatches(sms).first.group(0);
      return code;
    }
    return "NO SMS";
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Sms retriever example app'),
          backgroundColor: isListening ? Colors.green : Colors.amber,
        ),
        body: new Center(
          child: new Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              FutureBuilder(
                builder: (context, data) {
                  return Text('SIGNATURE: ${data.data}');
                },
                future: SmsRetriever.getAppSignature(),
              ),
              Text('SMS CODE: $_smsCode \n'),
              Text(
                  'Press the button below to start\nlistening for an incoming SMS'),
              new RaisedButton(
                onPressed: () async {
                  isListening = true;
                  setState(() {});
                  String smsCode = await SmsRetriever.startListening();
                  _smsCode = getCode(smsCode);
                  isListening = false;
                  setState(() {});
                  SmsRetriever.stopListening();
                },
                child: Text(isListening ? "STOP" : "START"),
              )
            ],
          ),
        ),
      ),
    );
  }
}
