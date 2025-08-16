package com.example.hellokotlinapp.notification



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.hellokotlinapp.R
import com.example.hellokotlinapp.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Event Reminder"

        val channelId = "reminders_channel"
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }

        val pi = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification) // add a small icon in res/drawable
            .setContentTitle("Reminder")
            .setContentText(title)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()

        nm.notify(System.currentTimeMillis().toInt(), notif)
    }
}
