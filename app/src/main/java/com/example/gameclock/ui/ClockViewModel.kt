package com.example.gameclock.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gameclock.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ClockViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClockUiState())
    val uiState: StateFlow<ClockUiState> = _uiState.asStateFlow()

    var stateApp by mutableStateOf(MainState())

    fun onThemeChange(event: ThemeChangeEvent) {
        when (event) {
            is ThemeChangeEvent.ThemeChange -> {
                stateApp = stateApp.copy(theme = event.theme)
            }
        }
    }

    fun toggleSeconds() {
        _uiState.update { currentState ->
            currentState.copy(
                showSeconds = !uiState.value.showSeconds
            )
        }
    }

    fun toggleAlarmButton() {
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmButton = !uiState.value.showAlarmButton
            )
        }
    }

    fun toggleTimerButton() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerButton = !uiState.value.showTimerButton
            )
        }
    }

    fun toggleClockTwelveHourFormat() {
        _uiState.update { currentState ->
            currentState.copy(
                clockFormatIsTwelveHour = !uiState.value.clockFormatIsTwelveHour
            )
        }
    }

    fun toggleFullScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                isFullScreen = !uiState.value.isFullScreen
            )
        }
    }

    fun updateClockScale(clockScale: Float) {
        _uiState.update { currentState ->
            currentState.copy(clockScale = clockScale)
        }
    }

    fun updateButtonsScale(buttonsScale: Float) {
        _uiState.update { currentState ->
            currentState.copy(buttonsScale = buttonsScale)
        }
    }


}

sealed class ThemeChangeEvent {
    data class ThemeChange(val theme: AppTheme) : ThemeChangeEvent()
}

data class MainState(
    val theme: AppTheme = AppTheme.Default,
)

data class ClockUiState(
    val showSeconds: Boolean = true,
    val showAnimations: Boolean = true,
    val clockFormatIsTwelveHour: Boolean = true,
    val isFullScreen: Boolean = false,
    val clockScale: Float = 1f,
    val buttonsScale: Float = 1f,
    val showAlarmButton: Boolean = true,
    val showTimerButton: Boolean = true,

    )

