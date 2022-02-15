package com.debut.sms_retriever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry.Registrar
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

class SmsRetrieverPlugin() : MethodCallHandler, FlutterPlugin {

    private var receiver: MySMSBroadcastReceiver? = null
    var sms: String? = null
    var result: MethodChannel.Result? = null
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding){
        context = binding.applicationContext
        channel = MethodChannel(binding.getBinaryMessenger(), "sms_retriever")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when {
            call.method == "getAppSignature" -> {
                val signature = AppSignatureHelper(context).getAppSignatures()[0]
                result.success(signature);
            }
            call.method == "startListening" -> {
                receiver = MySMSBroadcastReceiver()
                context.registerReceiver(receiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
                startListening()
                this.result = result;
            }
            call.method == "stopListening" -> unregister()
            else -> result.notImplemented()
        }
    }


    private fun startListening() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        val client = SmsRetriever.getClient(context)

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        val task = client.startSmsRetriever()

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener {
            // Successfully started retriever, expect broadcast intent
            Log.e(javaClass::getSimpleName.name, "task started")
        }

        task.addOnFailureListener {
            // Failed to start retriever, inspect Exception for more details
            Log.e(javaClass::getSimpleName.name, "task starting failed")
            // ...
        }
    }

    private fun unregister() {
        context.unregisterReceiver(receiver);
    };


    /**
     * BroadcastReceiver to wait for SMS messages. This can be registered either
     * in the AndroidManifest or at runtime.  Should filter Intents on
     * SmsRetriever.SMS_RETRIEVED_ACTION.
     */
    inner class MySMSBroadcastReceiver : BroadcastReceiver() {


        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status

                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        sms = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                        result?.success(sms)
                    }

                    CommonStatusCodes.TIMEOUT -> {
                    }
                }// Extract one-time code from the message and complete verification
                // by sending the code back to your server.
                // Waiting for SMS timed out (5 minutes)
                // Handle the error ...
            }
        }
    }
}

