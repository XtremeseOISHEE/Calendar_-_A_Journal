package com.example.hellokotlinapp.data.remote



import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    val currentUid: String? get() = auth.currentUser?.uid

    suspend fun signup(fullName: String, username: String, email: String, birthdate: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw IllegalStateException("No UID")

        // Display name optional
        result.user?.updateProfile(userProfileChangeRequest { displayName = fullName })?.await()

        val userDoc = mapOf(
            "uid" to uid,
            "fullName" to fullName,
            "username" to username,
            "email" to email,
            "birthdate" to birthdate
        )
        db.collection("users").document(uid).set(userDoc).await()
    }

    suspend fun login(emailOrUsername: String, password: String) {
        // For simplicity: treat input as email. If you truly want username login,
        // first query Firestore to map username -> email, then auth with email.
        auth.signInWithEmailAndPassword(emailOrUsername, password).await()
    }

    fun logout() { auth.signOut() }
}
