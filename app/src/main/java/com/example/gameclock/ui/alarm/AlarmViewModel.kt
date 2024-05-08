@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gameclock.ui.alarm

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameclock.data.alarms.AlarmRepository
import com.example.gameclock.data.alarms.GameClockAlarmManager
import com.example.gameclock.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the Alarm screen.
 * It handles the business logic for setting, updating and deleting alarms.
 *
 * @property alarmRepository The repository to manage alarm data injected by Hilt.
 * @property gameClockAlarmManager The manager to handle alarm operations injected by Hilt.
 */
@HiltViewModel
class AlarmViewModel
@Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val gameClockAlarmManager: GameClockAlarmManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val alarmUiState: StateFlow<AlarmUiState> = _uiState

    init {
        getAlarmsList()
        addToDateList()
    }

    /**
     * Fetches the list of alarms from the repository and updates the UI state.
     */
    private fun getAlarmsList() {
        viewModelScope.launch {
            alarmRepository.getAlarmsListFlow().collect { alarmsList ->
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

    /**
     * Sets a new alarm, updates the UI state, and saves the alarm in the repository.
     */
    fun setNewAlarm() {
        viewModelScope.launch {
            val lastId = alarmRepository.getLastId()
            val alarmTimePickerState = _uiState.value.timePickerState
            val alarmDateIndex = _uiState.value.dateListState.firstVisibleItemIndex

            val alarmDate = _uiState.value.dateList[alarmDateIndex]
            val calendar = Calendar.getInstance()
            val now = calendar.time

            calendar.time = alarmDate
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePickerState.hour)
            calendar.set(Calendar.MINUTE, alarmTimePickerState.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val alarmTime = calendar.time

            //Handles case of alarm set in past.
            //TODO: show user a message saying that the time is in past.
            if (alarmTime <= now) return@launch

            val format = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

            _uiState.update { currentState ->
                currentState.copy(
                    newAlarm = currentState.newAlarm.copy(
                        id = lastId?.plus(1) ?: 1,
                        title = "Alarm: ${format.format(alarmTime)}",
                        date = alarmTime,
                        isEnabled = true,
                    )
                )
            }
            alarmRepository.insertAlarm(_uiState.value.newAlarm)
            gameClockAlarmManager.setAlarm(_uiState.value.newAlarm)
            resetAlarmUiState()
        }
    }

    /**
     * Resets the UI state for the new alarm and the alarm to update.
     */
    private fun resetAlarmUiState() {
        _uiState.update { currentState ->
            currentState.copy(
                newAlarm = Alarm(),
                alarmToUpdate = Alarm(),
            )
        }
    }

    /**
     * Deletes an alarm from the repository and cancels it in the alarm manager.
     *
     * @param alarm The alarm to delete.
     */
    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.deleteAlarm(alarm)
            gameClockAlarmManager.cancel(alarm)
        }
    }

    /**
     * Updates an existing alarm by deleting the old one and setting a new one.
     */
    fun updateAlarm() {
        viewModelScope.launch {
            deleteAlarm(_uiState.value.alarmToUpdate)
            setNewAlarm()
        }
    }

    /**
     * Updates the time picker state in the UI state with the current time.
     */
    private fun updateTimePickerState() {
        _uiState.update { currentState ->
            currentState.copy(
                timePickerState = TimePickerState(
                    is24Hour = true,
                    initialHour = LocalTime.now().hour,
                    initialMinute = (LocalTime.now().minute + 1) % 60
                )
            )
        }
    }

    /**
     * Opens the set alarm popup and updates the UI state.
     */
    fun openSetAlarmPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showSetAlarmPopup = true,
            )
        }
        updateTimePickerState()
        resetAlarmPickerDate()

    }

    /**
     * Dismisses the set alarm popup and updates the UI state.
     */
    fun dismissAlarmPickerPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showSetAlarmPopup = false
            )
        }
    }

    /**
     * Opens the alarm update popup and updates the UI state with the selected alarm's data.
     *
     * @param alarm The alarm to update.
     * TODO: change update alarm popup to come up with correct date
     */

    fun openAlarmUpdatePopup(alarm: Alarm) {
        val calendar = Calendar.getInstance()
        calendar.time = alarm.date
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)
//        val dateIndex = _uiState.value.dateList.indexOf(alarm.date)
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmUpdatePopup = true,
                timePickerState = TimePickerState(
                    is24Hour = true,
                    initialHour = initialHour,
                    initialMinute = initialMinute
                ),
                alarmToUpdate = alarm,
//                dateListState = LazyListState(dateIndex)
            )
        }
    }

    /**
     * Dismisses the alarm update popup and updates the UI state.
     */
    fun dismissAlarmUpdatePopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmUpdatePopup = false
            )
        }
    }

    /**
     * Opens the alarm list popup and updates the UI state.
     */
    fun openAlarmListPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmListPopup = true
            )
        }
    }

    /**
     * Dismisses the alarm list popup and updates the UI state.
     */
    fun dismissAlarmListPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmListPopup = false
            )
        }
    }

    /**
     * Resets the date picker date in the UI state.
     */
    private fun resetAlarmPickerDate() {
        _uiState.update { currentState ->
            currentState.copy(
                dateListState = LazyListState()
            )
        }
    }

    /**
     * Adds dates to the date list in the UI state.
     *
     * @param startDate The start date to add dates from. If null, the current date is used.
     */
    fun addToDateList(startDate: Date? = null) {
        val dateList = _uiState.value.dateList.toMutableList()
        val newDates = if (startDate != null) {
            getRemainingDatesInMonthFromDate(startDate)
        } else {
            val calendar = Calendar.getInstance()
            val currentDate = calendar.time
            getRemainingDatesInMonthFromDate(currentDate)
        }
        // Filter out dates that are already in the list
        val uniqueDates = newDates.filter { it !in dateList }
        dateList += uniqueDates
        _uiState.update { currentState ->
            currentState.copy(
                dateList = dateList
            )
        }
    }

    /**
     * Gets the remaining dates in the month from a given start date.
     *
     * @param startDate The start date to get dates from.
     * @return A list of remaining dates in the month.
     */
    private fun getRemainingDatesInMonthFromDate(startDate: Date): List<Date> {
        Log.i("DateSelector", "getRemainingDatesInMonthFromDate: $startDate")
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        val dates = mutableListOf<Date>()
        val startingDateDay = calendar.get(Calendar.DAY_OF_MONTH)
        val daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in startingDateDay..daysInCurrentMonth) {
            dates.add((calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dates
    }

    fun openPermissionsRequestPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showPermissionsRequestPopup = true
            )
        }
    }

    fun dismissPermissionsRequestPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showPermissionsRequestPopup = false
            )
        }
    }
}

/**
 * Data class representing the UI state of the Alarm screen.
 *
 * @property alarmsList The list of alarms.
 * @property newAlarm The new alarm to set.
 * @property alarmToUpdate The alarm to update.
 * @property timePickerState The state of the time picker.
 * @property dateListState The state of the date list.
 * @property dateList The list of dates.
 * @property showSetAlarmPopup Whether the set alarm popup is shown.
 * @property showAlarmUpdatePopup Whether the alarm update popup is shown.
 * @property showAlarmListPopup Whether the alarm list popup is shown.
 */
data class AlarmUiState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val alarmsList: List<Alarm> = emptyList(),
    val newAlarm: Alarm = Alarm(),
    val alarmToUpdate: Alarm = Alarm(),
    val timePickerState: TimePickerState = TimePickerState(
        is24Hour = true,
        initialHour = LocalTime.now().hour,
        initialMinute = (LocalTime.now().minute + 1) % 60,
    ),
    val dateListState: LazyListState = LazyListState(),
    val dateList: List<Date> = emptyList(),
    val showSetAlarmPopup: Boolean = false,
    val showAlarmUpdatePopup: Boolean = false,
    val showAlarmListPopup: Boolean = false,
    val showPermissionsRequestPopup: Boolean = false
)
