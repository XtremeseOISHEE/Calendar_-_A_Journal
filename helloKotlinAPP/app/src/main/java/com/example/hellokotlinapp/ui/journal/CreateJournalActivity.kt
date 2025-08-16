package com.example.hellokotlinapp.ui.journal


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hellokotlinapp.data.local.AppDatabase
import com.example.hellokotlinapp.data.local.JournalEntity
import com.example.hellokotlinapp.data.local.Mood
import com.example.hellokotlinapp.data.remote.SyncRepository
import com.example.hellokotlinapp.databinding.ActivityCreateJournalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CreateJournalActivity : AppCompatActivity() {
    private lateinit var b: ActivityCreateJournalBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val appDb by lazy { AppDatabase.get(this) }
    private val syncRepo by lazy { SyncRepository(FirebaseFirestore.getInstance()) }
    private val vm by lazy {
        JournalViewModel(
            repo = com.example.hellokotlinapp.data.repo.JournalRepository(appDb, syncRepo),
            userIdProvider = { auth.currentUser?.uid ?: "local" },
            remoteUidProvider = { auth.currentUser?.uid }
        )
    }

    private var pickedImageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickedImageUri = it
            Glide.with(this).load(it).into(b.ivPreview)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCreateJournalBinding.inflate(layoutInflater)
        setContentView(b.root)

        // 5 moods (emoji labels in a spinner or radio group)
        val moods = listOf(Mood.HAPPY, Mood.SAD, Mood.CALM, Mood.ANGRY, Mood.EXCITED)
        b.moodGroup.check(b.rbHappy.id) // default

        b.btnAttach.setOnClickListener {
            pickImage.launch(arrayOf("image/*"))
        }

        b.btnSave.setOnClickListener {
            val text = b.etText.text.toString()
            val mood = when (b.moodGroup.checkedRadioButtonId) {
                b.rbHappy.id -> Mood.HAPPY
                b.rbSad.id -> Mood.SAD
                b.rbCalm.id -> Mood.CALM
                b.rbAngry.id -> Mood.ANGRY
                else -> Mood.EXCITED
            }

            val entity = JournalEntity(
                userId = "",
                text = text,
                imageUri = pickedImageUri?.toString(),
                mood = mood
            )

            MainScope().launch {
                vm.add(entity) {
                    finish()
                }
            }
        }
    }
}
