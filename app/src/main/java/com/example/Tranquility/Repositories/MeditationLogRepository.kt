package com.example.Tranquility.Repositories




class MeditationLogRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getMeditationLogs(): Flow<List<MeditationLog>> = flow {
        //fetching a query snapshot of meditation documents
        val snapshot = db.collection("meditationSessions").get().await()
        val meditationLogs = snapshot.documents.mapNotNull { document ->
            document.toObject(MeditationLog::class.java)
        }
        emit(meditationLogs)
    }.catch { e ->
        Log.e("MeditationLogRepository", "Error fetching meditation logs", e)
        emit(emptyList()) // return empty lists if fetch failed.
    }
}
