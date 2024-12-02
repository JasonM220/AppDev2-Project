package com.example.Tranquility.Repositories

import android.util.Log
import com.example.Tranquility.Models.MeditationLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MeditationLogRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance() //get user

    fun getMeditationLogs(): Flow<List<MeditationLog>> = flow {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            emit(emptyList()) //empty list if we couldn't get the user
            return@flow
        }

        val userId = currentUser.uid // gets the current id
        val snapshot = db.collection("meditationSessions")
            .whereEqualTo("userId", userId)
            //.orderBy("dateTime", com.google.firebase.firestore.Query.Direction.DESCENDING) not working atm, filter for logs
            .get()
            .await()

        val meditationLogs = snapshot.documents.mapNotNull { document ->
            document.toObject(MeditationLog::class.java)
        }

        //debugging shit
        Log.d("MeditationLogRepository", "Fetched documents: ${snapshot.documents}")
        emit(meditationLogs)
    }.catch { e ->
        Log.e("MeditationLogRepository", "Error fetching meditation logs", e)
        emit(emptyList()) // Return an empty list if the fetch fails
    }


    suspend fun deleteMeditationLog(log: MeditationLog) {
        try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                //debug if user can't be found
                Log.e("MeditationLogRepository", "No logged-in user found")
                return
            }

            val snapshot = db.collection("meditationSessions")
                .whereEqualTo("userId", currentUser.uid)
                .whereEqualTo("dateTime", log.dateTime)
                .get()
                .await()

            for (document in snapshot.documents) {
                db.collection("meditationSessions").document(document.id).delete().await()
            }
        } catch (e: Exception) {
            //debug if we can't delete the log
            Log.e("MeditationLogRepository", "Error deleting meditation log", e)
        }
    }
}