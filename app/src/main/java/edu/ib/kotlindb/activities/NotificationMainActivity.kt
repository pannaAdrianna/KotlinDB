package edu.ib.kotlindb.activities

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import edu.ib.kotlindb.AlarmReceiver
import edu.ib.kotlindb.R
import edu.ib.kotlindb.databinding.ActivityNotificationMainBinding
import java.util.*


class NotificationMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationMainBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        binding.btnSelectTimer.setOnClickListener {
            showPicker()
        }
        binding.btnSetTimer.setOnClickListener {
            setAlarm()
        }
        binding.btnCancelTimer.setOnClickListener {

            cancelAlarm()

        }


    }

    private fun cancelAlarm() {

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0,intent,0)

        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_LONG).show()


    }

    private fun showPicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time for SPF")
            .build()

        picker.show(supportFragmentManager, "INCI")

        picker.addOnPositiveButtonClickListener {

            if (picker.hour > 12) {
                binding.selectedTime.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + " PM"

            } else {
                binding.selectedTime.text =
                    String.format("%02d", picker.hour) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + " AM"

            }
            // instance of calendar
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0


        }
    }

    private fun setAlarm() {

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0,intent,0)

        alarmManager.setRepeating(

            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )

        Toast.makeText(this, "Alarm setted", Toast.LENGTH_SHORT).show()

    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name: CharSequence = "AppChannel"
            val description = "description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("spf", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)


            notificationManager.createNotificationChannel(channel)
        }
    }


}