package com.keremturker.notification.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {


        Toast.makeText(context, "Click Custom Notification", Toast.LENGTH_LONG).show()

    }
}