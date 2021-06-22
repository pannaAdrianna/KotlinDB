package edu.ib.kotlindb

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.ib.kotlindb.activities.DestinationActivity

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
//        TODO("Not yet implemented")

        val i = Intent(context, DestinationActivity::class.java)
        intent!!.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0,i,0)

        val builder = NotificationCompat.Builder(context!!, "spf")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("INCI App Alarm Manager")
            .setContentText(" Tekst")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())
    }


}