package com.mhappening.gameclock.ui.timer

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhappening.gameclock.data.alarms.GameClockAlarmManager
import com.mhappening.gameclock.data.timers.TimerRepository
import com.mhappening.gameclock.model.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TimerViewModel
@Inject constructor(
    private val timerRepository: TimerRepository,
//    private val gameClockAlarmManager: GameClockAlarmManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState

    private val TAG = "TimerViewModel"

    init {
        getTimersList()
    }

    private fun getTimersList() {
        viewModelScope.launch {
            timerRepository.timersList.collect { timerList ->
                _uiState.value = TimerUiState(
                    timerList = timerList
                )
            }

        }
    }

    fun setNewTimer() {
        viewModelScope.launch {
            val lastId = timerRepository.getLastId() ?: 0
            val timerDurationSeconds =
                (_uiState.value.newTimerDurationSeconds.value.toIntOrNull() ?: 0) +
                        ((_uiState.value.newTimerDurationMinutes.value.toIntOrNull() ?: 0) * 60) +
                        ((_uiState.value.newTimerDurationHours.value.toIntOrNull()
                            ?: 0) * 3600).toLong()

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, timerDurationSeconds.toInt())

            val newTimer = Timer(
                timerId = lastId + 1,
                durationSeconds = timerDurationSeconds,
                endTime = calendar.time,
                isEnabled = true
            )
            Log.i(TAG, "setNewTimer $newTimer")

            timerRepository.insertTimer(newTimer)

            //TODO: add timer notification logic here


            resetNewTimerState()
        }
    }

//    fun updateNewTimerDuration(hours: String = "", minutes: String = "", seconds: String = "") {
//        _uiState.update { currentState ->
//            currentState.copy(
//                newTimerDurationHours = mutableStateOf(hours),
//                newTimerDurationMinutes = mutableStateOf(minutes),
//                newTimerDurationSeconds = mutableStateOf(seconds)
//            )
//        }
//
//    }

    fun updateEndTime() {
        val timerDurationSeconds =
            (_uiState.value.newTimerDurationSeconds.value.toIntOrNull() ?: 0) +
                    ((_uiState.value.newTimerDurationMinutes.value.toIntOrNull() ?: 0) * 60) +
                    ((_uiState.value.newTimerDurationHours.value.toIntOrNull()
                        ?: 0) * 3600).toLong()
//        Log.i(TAG, "updateEndTime: $timerDurationSeconds")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, timerDurationSeconds.toInt())

        _uiState.update { currentState ->
            currentState.copy(
                newTimer = currentState.newTimer.copy(endTime = calendar.time)
            )
        }
    }

    fun isSetTimerEnabled(): Boolean {
        return _uiState.value.newTimerDurationSeconds.value.isNotEmpty() or _uiState.value.newTimerDurationMinutes.value.isNotEmpty() or _uiState.value.newTimerDurationHours.value.isNotEmpty()
    }

    fun resetNewTimerState() {
        _uiState.update { currentState ->
            currentState.copy(
                newTimerDurationSeconds = mutableStateOf(""),
                newTimerDurationMinutes = mutableStateOf(""),
                newTimerDurationHours = mutableStateOf("")
            )
        }
    }

    fun toggleTimerPausePlay(timer: Timer) {
        if (timer.isEnabled) {
            pauseTimer(timer)
        } else {
            resumeTimer(timer)
        }
    }

    fun pauseTimer(timer: Timer) {
        val remainingTime = timer.endTime.time - Date().time
        viewModelScope.launch {
            timerRepository.updateTimer(
                timer.copy(
                    remainingTimeOnPause = remainingTime,
                    isEnabled = false
                )
            )
        }
    }

    fun resumeTimer(timer: Timer) {
        val newEndTime = Date(Date().time + timer.remainingTimeOnPause)
        viewModelScope.launch {
            timerRepository.updateTimerEndTimeAndStatus(
                timerId = timer.timerId,
                endTime = newEndTime,
                isEnabled = true
            )
        }
    }

    fun cancelTimer(timer: Timer) {
        viewModelScope.launch {
            timerRepository.deleteTimer(timer)
        }
    }


//    fun deleteTimer(timer: Timer) {
//        timerRepository.deleteTimer(timer)
//    }
//
//    fun getLastId() = timerRepository.getLastId()
//
//    fun getTimerById(id: Int) = timerRepository.getTimerById(id)
//
//    fun getTimerFlowByID(id: Int) = timerRepository.getTimerFlowByID(id)
//
//    fun getTimerByDate(endTime: Long) = timerRepository.getTimerByDate(endTime)

    fun toggleTimerPickerPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerPickerPopup = true,
            )
        }
    }

    fun dismissTimerPickerPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerPickerPopup = false
            )
        }
    }

    fun toggleTimerListPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerListPopup = true,
            )
        }
        Log.i(TAG, "toggleTimerListPopup: ${uiState.value.showTimerListPopup}")
    }

    fun dismissTimerListPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerListPopup = false
            )
        }
        Log.i(TAG, "dismissTimerListPopup: ${uiState.value.showTimerListPopup}")
    }

    fun dismissSmallTimerRunning() {
        _uiState.update { currentState ->
            currentState.copy(
                showSmallTimerRunning = false
            )
        }
    }

    fun setSmallTimerRunning(timer: Timer) {
        _uiState.update { currentState ->
            currentState.copy(
                smallTimerRunning = timer,
                showSmallTimerRunning = true
            )
        }
    }
}

data class TimerUiState(
    val timerList: List<Timer> = emptyList(),
    val newTimer: Timer = Timer(),
    val newTimerDurationSeconds: MutableState<String> = mutableStateOf(""),
    val newTimerDurationMinutes: MutableState<String> = mutableStateOf(""),
    val newTimerDurationHours: MutableState<String> = mutableStateOf(""),
    val newTimerDurationSecondsInt: Int = newTimerDurationSeconds.value.toIntOrNull() ?: 0,
    val newTimerDurationMinutesInt: Int = newTimerDurationMinutes.value.toIntOrNull() ?: 0,
    val newTimerDurationHoursInt: Int = newTimerDurationHours.value.toIntOrNull() ?: 0,
    val showTimerPickerPopup: Boolean = false,
    val showTimerListPopup: Boolean = false,
    val showSmallTimerRunning: Boolean = false,
    val smallTimerRunning: Timer = Timer()

)