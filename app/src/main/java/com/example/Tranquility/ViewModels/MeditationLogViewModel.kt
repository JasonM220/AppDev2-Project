package com.example.Tranquility.ViewModels

class MeditationLogViewModel(private val repository: MeditationLogRepository) : ViewModel() {

    val meditationLogs: LiveData<List<MeditationLog>> = liveData {
        repository.getMeditationLogs().collect { logs ->
            emit(logs)
        }
    }
}
