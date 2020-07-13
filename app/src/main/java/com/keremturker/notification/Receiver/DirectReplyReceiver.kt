package com.keremturker.notification.Receiver

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.keremturker.notification.MainActivity

class DirectReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        //Kullanıcı veri girmişse
        if (remoteInput != null) {

            //Anahtar kelime ile kullanıcının girmiş olduğu texti alıyoruz.
            val text = remoteInput.getCharSequence(MainActivity.NOTIFICATION_REPLY)

            // Girilen texti ekrana yazdır
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()

            // İşlem tamamlandığında Bildirimin kapatılmasını sağlar
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(MainActivity.DIRECT_REPLY_NOTIFICATION_ID)
        }

    }
}