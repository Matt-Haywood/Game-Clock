package com.example.gameclock.ui.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gameclock.ClockApplication
import com.example.gameclock.data.alarms.AlarmRepository
import com.example.gameclock.model.Alarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val alarmUiState: StateFlow<AlarmUiState> = _uiState


    init {
        getAlarmsList()
    }

    fun getAlarmsList() {
        viewModelScope.launch {
            alarmRepository.getAlarmsList().collect { alarmsList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        alarmsList = alarmsList
                    )
                }
            }
        }

    }




    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.insertAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.deleteAlarm(alarm)
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.updateAlarm(alarm)
        }
    }

//    fun toggleAlarmPickerPopup() {
//        _uiState.update { currentState ->
//            currentState.copy(
//                showAlarmPickerPopup = !alarmUiState.value.showAlarmPickerPopup,
//            )
//        }
//
//    }
//    fun dismissAlarmPickerPopup() {
//        _uiState.update { currentState ->
//            currentState.copy(
//                showAlarmPickerPopup = false
//            )
//        }
//    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClockApplication)
                AlarmViewModel(
                    application.container.alarmRepository
                )
            }
        }
    }
}

data class AlarmUiState(
    val alarmsList: List<Alarm> = emptyList(),
//    val showAlarmPickerPopup: Boolean = false,
)