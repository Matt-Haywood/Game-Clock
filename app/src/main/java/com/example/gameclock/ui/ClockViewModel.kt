package com.example.gameclock.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameclock.data.UserPreferencesRepository
import com.example.gameclock.data.clockthemes.ClockThemeList
import com.example.gameclock.data.clockthemes.ClockThemePreferencesRepository
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockFormat
import com.example.gameclock.model.ClockThemePreferences
import com.example.gameclock.ui.theme.ClockFont
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Main ViewModel for the Clock screen.
 * This ViewModel is responsible for managing the state of the Clock screen and the settings of the clock/app.
 * The Fullscreen state is managed by the UserPreferencesRepository (DataStore) as this is an app-wide setting.
 * The Theming and per theme settings are managed from here and saved in the ClockThemePreferencesRepository (Room).
 */
@HiltViewModel
class ClockViewModel @Inject constructor(
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
        loadLastTheme()
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
                            appTheme = theme.appTheme,
                            clockFont = theme.clockFont,
                            thumbnail = theme.thumbnail,
                            showAnimations = theme.showAnimations,
                            clockFormat = theme.clockFormat,
                            clockScale = theme.clockScale,
                            buttonsScale = theme.buttonsScale,
                            showAlarmButton = theme.showAlarmButton,
                            showTimerButton = theme.showTimerButton
                        )
                    )
                }
            }
            userPreferencesRepository.fullscreen.collect { fullscreen ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isFullScreen = fullscreen
                    )
                }
            }
        }
    }

    private fun loadThemes() {
        backgroundCoroutineScope.launch {
            clockThemePreferencesRepository.getAllClockThemePreferences().collect { list ->
                themesPreferencesList = list.filterNotNull().toMutableList()
            }
//            _uiState.update { currentState ->
//                currentState.copy(
//                    themesPreferencesList = themesPreferencesList.toList()
//                )
//            }
            Log.i("themeChange", "loadThemes: Themes Loaded ${themesPreferencesList.size} ")
//            val lastOpenedTheme = userPreferencesRepository.lastOpenedTheme.firstOrNull()
//            if (lastOpenedTheme != null) {
//                onThemeChange(ThemeChangeEvent.ThemeChange(AppTheme.valueOf(lastOpenedTheme)))
//            }
        }

    }


    fun saveThemePreferences() {
        backgroundCoroutineScope.launch {
            clockThemePreferencesRepository.updateClockThemePreferences(
                ClockThemePreferences(
                    appTheme = clockUiState.value.theme,
                    showAnimations = clockUiState.value.showAnimations,
                    clockFormat = clockUiState.value.clockFormat,
                    clockScale = clockUiState.value.clockScale,
                    buttonsScale = clockUiState.value.buttonsScale,
                    showAlarmButton = clockUiState.value.showAlarmButton,
                    showTimerButton = clockUiState.value.showTimerButton,
                    clockFont = clockUiState.value.clockFont,
                    thumbnail = themesPreferencesList.first { it.appTheme == clockUiState.value.theme }.thumbnail

                    )
            )
        }
    }

    private fun loadLastTheme() {
        backgroundCoroutineScope.launch {
            val lastOpenedTheme = userPreferencesRepository.lastOpenedTheme.firstOrNull()
            if (lastOpenedTheme != null) {
//                val themePreferences =
//                    clockThemePreferencesRepository.getClockThemePreferences(lastOpenedTheme)
//                        .firstOrNull()
                val themeExists =
                    themesPreferencesList.any { it.appTheme.themeName == lastOpenedTheme }
                if (themeExists) {
                    onThemeChange(ThemeChangeEvent.ThemeChange(AppTheme.valueOf(lastOpenedTheme)))
                    Log.i("themeChange", "loadLastTheme: $lastOpenedTheme")
                } else {
                    Log.i(
                        "themeChange",
                        "loadLastTheme: theme $lastOpenedTheme doesn't exist in themesPreferencesList"
                    )
                }
            }
        }
    }


    fun onThemeChange(event: ThemeChangeEvent) {
        when (event) {
            is ThemeChangeEvent.ThemeChange -> {
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            theme = event.theme,
                            showAnimations = themesPreferencesList.first { it.appTheme == event.theme }.showAnimations,
                            clockFormat = themesPreferencesList.first { it.appTheme == event.theme }.clockFormat,
                            clockScale = themesPreferencesList.first { it.appTheme == event.theme }.clockScale,
                            buttonsScale = themesPreferencesList.first { it.appTheme == event.theme }.buttonsScale,
                            showAlarmButton = themesPreferencesList.first { it.appTheme == event.theme }.showAlarmButton,
                            showTimerButton = themesPreferencesList.first { it.appTheme == event.theme }.showTimerButton,
                            clockFont = themesPreferencesList.first { it.appTheme == event.theme }.clockFont,

                            )
                    }
                    userPreferencesRepository.setLastOpenedTheme(event.theme.themeName)
                    Log.i("themeChange", "LastOpenedTheme: ${event.theme}")

                }
            }
        }
    }

    /*    fun returnToDefaultTheme() {
            _uiState.update { currentState ->
                currentState.copy(
                    theme = AppTheme.Default,
                )
            }
        }*/

    fun resetThemeToDefaults() {
        viewModelScope.launch {
            _uiState.update { ui ->
                ui.copy(
                    showAnimations = true,
                    clockFormat = ClockFormat.TWENTY_FOUR_HOUR,
                    clockScale = 1.8f,
                    buttonsScale = 1.2f,
                    showAlarmButton = true,
                    showTimerButton = true,
                    clockFont = ClockThemeList().loadThemes()
                        .first { it.appTheme == _uiState.value.theme }.clockFont

                )
            }
        }
    }


    // Create a job to hold the coroutine that hides the buttons
    private var hideButtonsJob: Job? = null

    init {
        // Call resetTimer initially to start the timer
        showButtons()
    }

    // Function to reset the timer
    fun resetHideButtonsTimer() {
        // Cancel the existing job if it's not null
        hideButtonsJob?.cancel()

        // Start a new job to hide the buttons after 7 seconds
        hideButtonsJob = viewModelScope.launch {
            delay(7000)  // Wait for 7 seconds
            _uiState.update { currentState ->
                currentState.copy(
                    buttonsVisible = false
                )
            }
        }
    }

    // Function to show the buttons and reset the timer
    fun showButtons() {
        _uiState.update { currentState ->
            currentState.copy(
                buttonsVisible = true
            )
        }
        resetHideButtonsTimer()
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

    fun updateClockFormat(clockFormat: ClockFormat) {
        _uiState.update { currentState ->
            currentState.copy(
                clockFormat = clockFormat
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

    /*    fun toggleAlarmPickerPopup() {
            _uiState.update { currentState ->
                currentState.copy(
                    showSetAlarmPopup = !clockUiState.value.showSetAlarmPopup,
                    showTimerPickerPopup = false
                )
            }
        }

        fun dismissAlarmPickerPopup() {
            _uiState.update { currentState ->
                currentState.copy(
                    showSetAlarmPopup = false
                )
            }
        }

        fun toggleAlarmUpdatePopup() {
            _uiState.update { currentState ->
                currentState.copy(
                    showAlarmUpdatePopup = !clockUiState.value.showAlarmUpdatePopup
                )
            }
        }

        fun dismissAlarmUpdatePopup() {
            _uiState.update { currentState ->
                currentState.copy(
                    showAlarmUpdatePopup = false
                )
            }
        }*/

    fun toggleTimerPickerPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                showTimerPickerPopup = !clockUiState.value.showTimerPickerPopup,
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

    fun updateClockFont(clockFont: ClockFont) {
        _uiState.update { currentState ->
            currentState.copy(clockFont = clockFont)
        }
    }
}


sealed class ThemeChangeEvent {
    data class ThemeChange(val theme: AppTheme) : ThemeChangeEvent()
}


data class ClockUiState(
    val theme: AppTheme = AppTheme.Light,
    val showAnimations: Boolean = true,
    val clockFormat: ClockFormat = ClockFormat.TWENTY_FOUR_HOUR,
    val isFullScreen: Boolean = false,
    val clockScale: Float = 1.8f,
    val buttonsScale: Float = 1.2f,
    val showAlarmButton: Boolean = true,
    val showTimerButton: Boolean = true,
    val buttonsVisible: Boolean = true,
    val showTimerPickerPopup: Boolean = false,
    val clockFont: ClockFont = ClockFont.ROBOTO

)

