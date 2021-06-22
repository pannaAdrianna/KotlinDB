package edu.ib.kotlindb

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.ib.kotlindb.activities.DestinationActivity


class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
//        TODO("Not yet implemented")

        val i = Intent(context, DestinationActivity::class.java)
        intent!!.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)

        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val builder = NotificationCompat.Builder(context!!, "spf")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("INCI App")
            .setContentText("Put SPF!")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//            { delay, vibrate, sleep, vibrate, sleep }
            .setVibrate(longArrayOf(0, 1000, 1000, 1000, 1000))




        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())
    }


}