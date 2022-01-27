package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyRec: BroadcastReceiver() {
    val CHANNEL_ID = "CHANNEL_ID"
    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context?, intent: Intent?) {
        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.i)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
        notification.setContentTitle("Notification")
        notification.setContentText("Check for updates")
        notification.setPriority(NotificationCompat.PRIORITY_HIGH)
        notification.setSmallIcon(R.drawable.ic_baseline_category_24)
        notification.setLargeIcon(bitmap)
        notification.setStyle(
            NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap).bigLargeIcon(null))
        notification.setContentIntent(pendingIntent)
        notification.setVisibility(Notification.VISIBILITY_PUBLIC)
        notification.setAutoCancel(true)
        notification.addAction(R.drawable.ic_baseline_edit_24, "More", pendingIntent)
        if(Build.VERSION.SDK_INT >= 21){
            notification?.setVibrate(LongArray(0))
        }
        val noti: Notification = notification!!.build()

        val notiManager = NotificationManagerCompat.from(context)
        notiManager.notify(1, noti)
    }
}