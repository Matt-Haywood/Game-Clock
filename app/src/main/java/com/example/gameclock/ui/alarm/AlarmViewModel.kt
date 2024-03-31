package com.example.gameclock.ui.alarm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameclock.data.alarms.AlarmRepository
import com.example.gameclock.data.alarms.SetAlarm
import com.example.gameclock.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel
    @Inject constructor(
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
                alarmsList.forEach {
                    if (!it.isEnabled) {
                        deleteAlarm(it)
                    }
                }
                val filteredAlarmsList = alarmsList.filter { alarm -> alarm.isEnabled }
                _uiState.update { currentState ->
                    currentState.copy(
                        alarmsList = filteredAlarmsList
                    )
                }
            }
        }
    }


    fun setAlarm(alarm: Alarm, context: Context) {
        viewModelScope.launch {
            alarmRepository.insertAlarm(alarm)
            SetAlarm(context, alarm)
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

}

data class AlarmUiState(
    val alarmsList: List<Alarm> = emptyList(),
//    val showAlarmPickerPopup: Boolean = false,
)