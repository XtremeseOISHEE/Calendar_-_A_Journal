package com.example.hellokotlinapp.ui.event

import android.util.Log
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hellokotlinapp.data.local.AppDatabase
import com.example.hellokotlinapp.data.local.EventEntity
import com.example.hellokotlinapp.data.remote.SyncRepository
import com.example.hellokotlinapp.databinding.ActivityCreateEventBinding
import com.example.hellokotlinapp.notification.scheduleReminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CreateEventActivity : AppCompatActivity() {
    private lateinit var b: ActivityCreateEventBinding

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val appDb by lazy { AppDatabase.get(this) }
    private val syncRepo by lazy { SyncRepository(FirebaseFirestore.getInstance()) }
    private val vm by lazy {
        EventViewModel(
            repo = com.example.hellokotlinapp.data.repo.EventRepository(appDb, syncRepo),
            userIdProvider = { auth.currentUser?.uid ?: "local" },
            remoteUidProvider = { auth.currentUser?.uid }
        )
    }

    private var pickedCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Date picker
        b.etDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                b.etDate.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
                pickedCalendar.set(Calendar.YEAR, y)
                pickedCalendar.set(Calendar.MONTH, m)
                pickedCalendar.set(Calendar.DAY_OF_MONTH, d)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time picker
        b.etTime.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, h, min ->
                b.etTime.setText(String.format("%02d:%02d", h, min))
                pickedCalendar.set(Calendar.HOUR_OF_DAY, h)
                pickedCalendar.set(Calendar.MINUTE, min)
                pickedCalendar.set(Calendar.SECOND, 0)
                pickedCalendar.set(Calendar.MILLISECOND, 0)
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        // Save button click
        b.btnSave.setOnClickListener {
            val title = b.etTitle.text.toString().trim()
            val date = b.etDate.text.toString().trim()
            val time = b.etTime.text.toString().trim()
            val trigger = pickedCalendar.timeInMillis

            if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Use FirebaseAuth UID for userId
            val entity = EventEntity(
                userId = auth.currentUser?.uid ?: "local", // IMPORTANT: Firebase UID
                title = title,
                dateIso = date,
                timeIso = time,
                reminderAtMillis = trigger
            )

            vm.addEvent(entity) {
                // schedule notification
                scheduleReminder(this, trigger, title)
                Log.d("CreateEvent", "Event added: $entity")
                Toast.makeText(this, "Event saved & reminder set!", Toast.LENGTH_SHORT).show()
                finish() // back to EventListActivity
            }
        }
    }
}
