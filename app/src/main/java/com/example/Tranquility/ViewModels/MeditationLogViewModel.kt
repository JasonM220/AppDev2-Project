package com.example.Tranquility.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Tranquility.Models.MeditationLog
import com.example.Tranquility.Repositories.MeditationLogRepository
import kotlinx.coroutines.launch

class MeditationLogViewModel(private val repository: MeditationLogRepository) : ViewModel() {

    private val _meditationLogs = MutableLiveData<List<MeditationLog>>()
    val meditationLogs: LiveData<List<MeditationLog>> = _meditationLogs

    init {
        fetchMeditationLogs()
    }

    private fun fetchMeditationLogs() {
        viewModelScope.launch {
            repository.getMeditationLogs().collect { logs ->
                _meditationLogs.value = logs
            }
        }
    }

    fun deleteMeditationLog(log: MeditationLog) {
        viewModelScope.launch {
            repository.deleteMeditationLog(log)
            fetchMeditationLogs() //refresh upon deletion.
        }
    }

    fun addMeditationLog(log: MeditationLog) {
        viewModelScope.launch {
            repository.addMeditationLog(log)
            fetchMeditationLogs() // Refresh logs after adding a new one
        }
    }

}
