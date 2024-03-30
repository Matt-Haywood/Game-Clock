package com.example.gameclock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gameclock.ClockApplication
import com.example.gameclock.data.UserPreferencesRepository
import com.example.gameclock.data.clockthemes.ClockThemeList
import com.example.gameclock.data.clockthemes.ClockThemePreferencesRepository
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


/**
 * Main ViewModel for the Clock screen.
 * This ViewModel is responsible for managing the state of the Clock screen and the settings of the clock/app.
 * The Fullscreen state is managed by the UserPreferencesRepository (DataStore) as this is an app-wide setting.
 * The Theming and per theme settings are managed from here and saved in the ClockThemePreferencesRepository (Room).
 */
class ClockViewModel(
    private val clockThemePreferencesRepository: ClockThemePreferencesRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClockUiState())

    val clockUiState: StateFlow<ClockUiState> = _uiState

    private val backgroundCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
//    var stateApp by mutableStateOf(MainState())

    private var themesPreferencesList = mutableListOf<ClockThemePreferences>()

    // pre-loads the themes and fullscreen settings
    init {
        firstLoad()
        loadThemes()
    }

    private fun firstLoad() {
        viewModelScope.launch {
            val clockThemeList = ClockThemeList().loadThemes()
            clockThemeList.forEach { theme ->
                // check if the theme preferences exist, if not create them as defaults. This should only run on the first start up or if the app has been updated to include more themes.
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
            userPreferencesRepository.fullscreen.collect() { fullscreen ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isFullScreen = fullscreen
                    )
                }
            }
        }
    }

    fun resetThemeToDefaults() {
        viewModelScope.launch {
//            clockThemePreferencesRepository.updateClockThemePreferences(
//                ClockThemePreferences(
//                    _uiState.value.theme,
//                )
//            )
            _uiState.update {
                it.copy(
                    showSeconds = false,
                    showAnimations = true,
                    clockFormatIsTwelveHour = false,
                    clockScale = 1.8f,
                    buttonsScale = 1.2f,
                    showAlarmButton = true,
                    showTimerButton = true,
                )
            }
        }
    }

    private fun loadThemes() {
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


    fun saveThemePreferences() {
        viewModelScope.launch {
            clockThemePreferencesRepository.updateClockThemePreferences(
                ClockThemePreferences(
                    appTheme = clockUiState.value.theme,
                    showSeconds = clockUiState.value.showSeconds,
                    showAnimations = clockUiState.value.showAnimations,
                    clockFormatIsTwelveHour = clockUiState.value.clockFormatIsTwelveHour,
                    clockScale = clockUiState.value.clockScale,
                    buttonsScale = clockUiState.value.buttonsScale,
                    showAlarmButton = clockUiState.value.showAlarmButton,
                    showTimerButton = clockUiState.value.showTimerButton

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
                        showTimerButton = themesPreferencesList.first { it.appTheme == event.theme }.showTimerButton

                    )
                }
            }
        }
    }


    // Change settings
    fun toggleSeconds() {
        _uiState.update { currentState ->
            currentState.copy(
                showSeconds = !clockUiState.value.showSeconds
            )
        }

    }

    fun toggleShowAnimations() {
        _uiState.update { currentState ->
            currentState.copy(
                showAnimations = !clockUiState.value.showAnimations
            )
        }

    }

    fun toggleAlarmButton() {
        _uiState.update { currentState ->
            currentState.copy(
                showAlarmButton = !clockUiState.value.showAlarmButton
            )
        }
    }

    fun toggleTimerButton() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerButton = !clockUiState.value.showTimerButton
            )
        }
    }

    fun toggleClockTwelveHourFormat() {
        _uiState.update { currentState ->
            currentState.copy(
                clockFormatIsTwelveHour = !clockUiState.value.clockFormatIsTwelveHour
            )
        }
    }

    fun toggleFullScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                isFullScreen = !clockUiState.value.isFullScreen
            )

        }
        viewModelScope.launch { userPreferencesRepository.setFullscreen(clockUiState.value.isFullScreen) }

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
                showAlarmPickerPopup = !clockUiState.value.showAlarmPickerPopup,
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
                showTimerPickerPopup = !clockUiState.value.showTimerPickerPopup,
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
                ClockViewModel(
                    application.container.clockThemePreferencesRepository,
                    application.userPreferencesRepository
                )
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
    val showSeconds: Boolean = false,
    val showAnimations: Boolean = true,
    val clockFormatIsTwelveHour: Boolean = false,
    val isFullScreen: Boolean = false,
    val clockScale: Float = 1.8f,
    val buttonsScale: Float = 1.2f,
    val showAlarmButton: Boolean = true,
    val showTimerButton: Boolean = true,
    val showAlarmPickerPopup: Boolean = false,
    val showTimerPickerPopup: Boolean = false,

    )

