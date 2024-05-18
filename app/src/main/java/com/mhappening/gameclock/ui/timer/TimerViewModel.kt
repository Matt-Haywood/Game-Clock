package com.mhappening.gameclock.ui.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhappening.gameclock.data.alarms.GameClockAlarmManager
import com.mhappening.gameclock.data.timers.TimerRepository
import com.mhappening.gameclock.model.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TimerViewModel
@Inject constructor(
    private val timerRepository: TimerRepository,
    private val gameClockAlarmManager: GameClockAlarmManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState

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
            val lastId = timerRepository.getLastId()
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

}

data class TimerUiState constructor(
    val timerList: List<Timer> = emptyList(),
    val newTimer: Timer = Timer(),
)