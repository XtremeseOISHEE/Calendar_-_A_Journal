package com.example.hellokotlinapp.notification



import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun scheduleReminder(context: Context, triggerAtMillis: Long, title: String) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("title", title)
    }
    val pi = PendingIntent.getBroadcast(
        context,
        title.hashCode(), // unique
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
}
