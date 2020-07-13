package com.keremturker.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.RemoteViews
import com.keremturker.notification.Receiver.CustomNotificationReceiver
import com.keremturker.notification.Receiver.DirectReplyReceiver
import com.keremturker.notification.Receiver.NotificationReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val NOTIFICATION_CHANNELID = "com.keremturker.smsfilter"
    private val CHANNEL_DESCRIPTION = "Test notification"

    private val BASIC_NOTIFICATION_ID = 1234
    private val CLICKABLE_NOTIFICATION_ID = 12345
    private val ACTION_BUTTON_NOTIFICATION_ID = 123456
    private val EXPANDABLE_TEXT_NOTIFICATION_ID = 1234568
    private val EXPANDABLE_PICTURE_NOTIFICATION_ID = 12345689
    private val PROGRESSBAR_NOTIFICATION_ID = 123456890
    private val CUSTOM_NOTIFICATION_ID = 1234568900

    companion object {
        val NOTIFICATION_REPLY = "REPLY"
        val DIRECT_REPLY_NOTIFICATION_ID = 1234567


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //NotificationManger nesnesini initialize ediyoruz
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createChannel()


        basicnotif.setOnClickListener {
            basicNotif(this@MainActivity)
        }
        clickablenotif.setOnClickListener {
            clikableNotif(this@MainActivity)
        }

        actionnotif.setOnClickListener {
            actionButtonNotif(this@MainActivity)
        }

        directreplynotif.setOnClickListener {
            directReplyNotif(this@MainActivity)

        }
        expandabletextnotif.setOnClickListener {

            expandableTextNotif(this@MainActivity)

        }
        expandablepicturenotif.setOnClickListener {
            expandablePictureNotif(this@MainActivity)
        }

        progressnotif.setOnClickListener {
            progressBarNotif(this@MainActivity)
        }

        customnotif.setOnClickListener {
            customNotif(this@MainActivity)
        }


    }

    private fun createChannel() {

        //Android 8.0 'dan sonraki sürümler için bu kısım zorunludur.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNELID,
                    CHANNEL_DESCRIPTION,
                    NotificationManager.IMPORTANCE_HIGH
                )

            notificationManager.createNotificationChannel(notificationChannel)

        }


    }

    private fun basicNotif(context: Context) {


        builder = Notification.Builder(context)
            .setContentTitle("Bildirim Başlığı")
            .setContentText("Bildirim İçeriği")
            .setSmallIcon(R.drawable.envelope) // Bildirim ikonu
            .setShowWhen(true) //Bildirim zaman bilgisi
            .setCategory(Notification.CATEGORY_MESSAGE) //Bildirim kategorisi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }
        notificationManager.notify(BASIC_NOTIFICATION_ID, builder.build())

    }

    private fun clikableNotif(context: Context) {

        //Bildirime tıklayınca gidilecek activity
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder = Notification.Builder(context)
            .setContentTitle("Bildirim Başlığı")
            .setContentText("Bildirim İçeriği")
            .setSmallIcon(R.drawable.envelope) // Bildirim ikonu
            .setShowWhen(true) //Bildirim zaman bilgisi
            .setCategory(Notification.CATEGORY_MESSAGE) //Bildirim kategorisi
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Bildirime tıklanıldığında bildirimin kaldırılmasını sağlar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }
        notificationManager.notify(CLICKABLE_NOTIFICATION_ID, builder.build())


    }

    private fun actionButtonNotif(context: Context) {

        //Action Button Receiver
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        val actionIntent = PendingIntent.getBroadcast(
            this,
            0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder = Notification.Builder(context)
            .setContentTitle("Bildirim Başlığı")
            .setContentText("Bildirim İçeriği")
            .setSmallIcon(R.drawable.envelope) // Bildirim ikonu
            .setShowWhen(true) //Bildirim zaman bilgisi
            .setCategory(Notification.CATEGORY_MESSAGE) //Bildirim kategorisi
            .addAction(R.drawable.double_tick, "Okundu olarak işaretle", actionIntent)
            .setAutoCancel(true) // Bildirime tıklanıldığında bildirimin kaldırılmasını sağlar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }
        notificationManager.notify(ACTION_BUTTON_NOTIFICATION_ID, builder.build())


    }

    private fun directReplyNotif(context: Context) {


        builder = Notification.Builder(context)
            .setContentTitle("Bildirim Başlığı")
            .setContentText("Bildirim İçeriği")
            .setSmallIcon(R.drawable.envelope) // Bildirim ikonu
            .setShowWhen(true) //Bildirim zaman bilgisi
            .setCategory(Notification.CATEGORY_MESSAGE)  //Bildirim kategorisi
            .setAutoCancel(true) // Bildirime tıklanıldığında bildirimin kaldırılmasını sağlar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }

        //Bildirimdeki yanıtlama butonuna tıklama durumu kontrol edilmesini sağlar
        var remoteInput = RemoteInput.Builder(NOTIFICATION_REPLY).setLabel("Yanıtla").build()

        //Action Button Receiver
        var replyIntent = Intent(this, DirectReplyReceiver::class.java)
        var replyPendingIntent =
            PendingIntent.getBroadcast(this, 0, replyIntent, PendingIntent.FLAG_ONE_SHOT)


        //Ekranda gösterilecek action button nesnesi
        var action = Notification.Action.Builder(
            R.drawable.envelope,
            "Yanıtla",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()


        builder.addAction(action)

        notificationManager.notify(DIRECT_REPLY_NOTIFICATION_ID, builder.build())


    }

    private fun expandableTextNotif(context: Context) {


        //Bitmap nesnesi
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.large_picture
        )


        // Bildirim şablonu
        var bigTextStyle = Notification.BigTextStyle()
            .bigText(resources.getString(R.string.lorem))
            .setBigContentTitle("Big title")
            .setSummaryText("Summary TEXT")

        builder = Notification.Builder(context)
            .setContentTitle("Bildirim Başlığı")
            .setContentText(resources.getString(R.string.lorem))
            .setSmallIcon(R.drawable.envelope) // Bildirim ikonu
            .setLargeIcon(bitmap) // Bildirim büyük resmi
            .setShowWhen(true)  //Bildirim zaman bilgisi
            .setCategory(Notification.CATEGORY_MESSAGE) //Bildirim kategorisi
            .setAutoCancel(true)  // Bildirime tıklanıldığında bildirimin kaldırılmasını sağlar
            .setStyle(bigTextStyle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }

        notificationManager.notify(EXPANDABLE_TEXT_NOTIFICATION_ID, builder.build())
    }

    private fun expandablePictureNotif(context: Context) {

        //Bitmap nesnesi
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.large_picture
        )
        val bitmapNull: Bitmap? = null

        // Bildirim şablonu
        var bigPictureStyle = Notification.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(bitmapNull)
            .setBigContentTitle("Big Title")
            .setSummaryText("Summary Text")

        builder = Notification.Builder(context)
            .setContentTitle("Bildirim Başlığı")
            .setContentText(resources.getString(R.string.lorem))
            .setSmallIcon(R.drawable.envelope)  // Bildirim ikonu
            .setLargeIcon(bitmap) // Bildirim büyük resmi
            .setShowWhen(true) //Bildirim zaman bilgisi
            .setCategory(Notification.CATEGORY_MESSAGE) //Bildirim kategorisi
            .setAutoCancel(true) // Bildirime tıklanıldığında bildirimin kaldırılmasını sağlar
            .setStyle(bigPictureStyle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }

        notificationManager.notify(EXPANDABLE_PICTURE_NOTIFICATION_ID, builder.build())
    }

    private fun progressBarNotif(context: Context) {


        var Progressmax = 100


        builder = Notification.Builder(context)
            .setContentTitle("Resim indir")
            .setContentText("İndirme devam ediyor")
            .setShowWhen(true) //Bildirim zaman bilgisi
            .setProgress(Progressmax, 0, false)
            .setCategory(Notification.CATEGORY_PROGRESS)  //Bildirim kategorisi
            .setOnlyAlertOnce(true) // Bildirim birkez gösterilsin.
            .setSmallIcon(R.drawable.download)  // Bildirim ikonu
            .setOngoing(true) // Bildirimin ekrandan kaldırılamamasını sağlar


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }

        notificationManager.notify(PROGRESSBAR_NOTIFICATION_ID, builder.build())


        // Saniyede 1 defa progressBarı arttırarak bildirimi güncellemeyi sağlar
        Thread(Runnable {
            SystemClock.sleep(1000)
            var progress = 0
            while (progress <= Progressmax) {

                builder.setProgress(Progressmax, progress, false)
                notificationManager.notify(PROGRESSBAR_NOTIFICATION_ID, builder.build())


                SystemClock.sleep(1000)
                progress += 20
            }
            builder.setContentText("Download finished")
                .setProgress(0, 0, false)
                .setOngoing(false)
            notificationManager.notify(PROGRESSBAR_NOTIFICATION_ID, builder.build())
        }).start()
    }

    private fun customNotif(context: Context) {

        //Action Button
        val broadcastIntent = Intent(this, CustomNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Özelleştirilmiş tasarım
        val contentView = RemoteViews(context.packageName, R.layout.custom_notification)
        contentView.setTextViewText(R.id.tv_title, "Bildirim Başlığı")
        contentView.setTextViewText(R.id.tv_content, "Bildirim İçeriği")
        contentView.setImageViewResource(R.id.img_content, R.drawable.large_picture)

        contentView.setOnClickPendingIntent(R.id.tv_title, pendingIntent)



        builder = Notification.Builder(context)
            .setContent(contentView)
            .setContentTitle("") //Bildirim Başlığı
            .setContentText("") // Bildirim İçeriği
            .setCategory(Notification.CATEGORY_MESSAGE) //Bildirim kategorisi
            .setSmallIcon(R.drawable.envelope) // Bildirim ikonu


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNELID)
        }

        notificationManager.notify(CUSTOM_NOTIFICATION_ID, builder.build())


    }

}
