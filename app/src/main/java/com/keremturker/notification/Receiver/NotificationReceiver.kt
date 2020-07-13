package com.keremturker.notification.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {


        Toast.makeText(context, "Okundu olarak işaretlendi", Toast.LENGTH_LONG).show()

    }
}