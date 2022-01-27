package com.example.myapplication.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.renderscript.RenderScript
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.core.content.ContextCompat.getSystemService
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import java.util.GregorianCalendar.from

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MainActivity::class.java)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)


        val builder = NotificationCompat.Builder(context!!, "photo-eng")
            .setSmallIcon(R.drawable.ic_baseline_android_24)
            .setContentTitle("Уведомление")
            .setContentText("Время повторить изученное")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        val notifyManager = NotificationManagerCompat.from(context)
        notifyManager.notify(123, builder.build())
    }



}