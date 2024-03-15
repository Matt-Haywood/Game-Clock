package com.example.gameclock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gameclock.ClockApplication
import com.example.gameclock.data.ClockThemeList
import com.example.gameclock.data.ClockThemePreferencesRepository
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockThemePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClockViewModel(
    private val clockThemePreferencesRepository: ClockThemePreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClockUiState())

    val uiState: StateFlow<ClockUiState> = _uiState

    private val backgroundCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
//    var stateApp by mutableStateOf(MainState())

    private var themesPreferencesList = mutableListOf<ClockThemePreferences>()


    init {
        firstLoad()
        loadThemes()
    }

    private fun firstLoad() {
        viewModelScope.launch {
            val clockThemeList = ClockThemeList().loadThemes()
            clockThemeList.forEach { theme ->
                val themePreferences =
                    clockThemePreferencesRepository.getClockThemePreferences(theme.appTheme)
                        .firstOrNull()
                if (themePreferences == null) {
                    clockThemePreferencesRepository.writeClockThemePreferences(
                        ClockThemePreferences(
                            theme.appTheme
                        )
                    )
                }
            }
            val themePreferences =
                clockThemePreferencesRepository.getClockThemePreferences(AppTheme.Default)
                    .firstOrNull()
            if (themePreferences == null) {
                clockThemePreferencesRepository.writeClockThemePreferences(
                    ClockThemePreferences(
                        AppTheme.Default
                    )
                )
            }
        }
    }
//    private fun firstLoad() {
//        viewModelScope.launch {
//            if (clockThemePreferencesRepository.isDatabaseEmpty()) {
//                val clockThemeList = ClockThemeList().loadThemes()
//                clockThemeList.forEach { theme ->
//                    clockThemePreferencesRepository.writeClockThemePreferences(
//                        ClockThemePreferences(
//                            theme.appTheme
//                        )
//                    )
//                }
//            }
//        }
//    }

    fun loadThemes() {
        viewModelScope.launch {
            clockThemePreferencesRepository.getAllClockThemePreferences().collect { list ->
                themesPreferencesList = list.filterNotNull().toMutableList()
            }
            _uiState.update { currentState ->
                currentState.copy(
                    themesPreferencesList = themesPreferencesList
                )
            }
        }
    }

    //TODO - Add logic to save theme preferences
    fun saveThemePreferences() {
        viewModelScope.launch {
            clockThemePreferencesRepository.updateClockThemePreferences(
                ClockThemePreferences(
                    appTheme = uiState.value.theme,
                    showSeconds = uiState.value.showSeconds,
                    showAnimations = uiState.value.showAnimations,
                    clockFormatIsTwelveHour = uiState.value.clockFormatIsTwelveHour,
                    clockScale = uiState.value.clockScale,
                    buttonsScale = uiState.value.buttonsScale,
                    showAlarmButton = uiState.value.showAlarmButton,
                    showTimerButton = uiState.value.showTimerButton,
                    isFullScreen = uiState.value.isFullScreen

                )
            )
        }
    }


    fun onThemeChange(event: ThemeChangeEvent) {
        when (event) {
            is ThemeChangeEvent.ThemeChange -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        theme = event.theme,
                        showSeconds = themesPreferencesList.first { it.appTheme == event.theme }.showSeconds,
                        showAnimations = themesPreferencesList.first { it.appTheme == event.theme }.showAnimations,
                        clockFormatIsTwelveHour = themesPreferencesList.first { it.appTheme == event.theme }.clockFormatIsTwelveHour,
                        clockScale = themesPreferencesList.first { it.appTheme == event.theme }.clockScale,
                        buttonsScale = themesPreferencesList.first { it.appTheme == event.theme }.buttonsScale,
                        showAlarmButton = themesPreferencesList.first { it.appTheme == event.theme }.showAlarmButton,
                        showTimerButton = themesPreferencesList.first { it.appTheme == event.theme }.showTimerButton,
                        isFullScreen = themesPreferencesList.first { it.appTheme == event.theme }.isFullScreen

                    )
                }
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

    fun toggleAlarmPickerPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmPickerPopup = !uiState.value.showAlarmPickerPopup,
                showTimerPickerPopup = false
            )
        }
    }

    fun dismissAlarmPickerPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmPickerPopup = false
            )
        }
    }

    fun toggleTimerPickerPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerPickerPopup = !uiState.value.showTimerPickerPopup,
                showAlarmPickerPopup = false
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClockApplication)
                ClockViewModel(application.container.clockThemePreferencesRepository)
            }
        }
    }


}

sealed class ThemeChangeEvent {
    data class ThemeChange(val theme: AppTheme) : ThemeChangeEvent()
}


data class ClockUiState(
    val themesPreferencesList: List<ClockThemePreferences> = emptyList(),
    val theme: AppTheme = AppTheme.Default,
    val showSeconds: Boolean = true,
    val showAnimations: Boolean = true,
    val clockFormatIsTwelveHour: Boolean = true,
    val isFullScreen: Boolean = false,
    val clockScale: Float = 1f,
    val buttonsScale: Float = 1f,
    val showAlarmButton: Boolean = true,
    val showTimerButton: Boolean = true,
    val showAlarmPickerPopup: Boolean = false,
    val showTimerPickerPopup: Boolean = false,

    )

