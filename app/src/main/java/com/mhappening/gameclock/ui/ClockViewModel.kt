package com.mhappening.gameclock.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhappening.gameclock.data.UserPreferencesRepository
import com.mhappening.gameclock.data.clockthemes.ClockThemeList
import com.mhappening.gameclock.data.clockthemes.ClockThemePreferencesRepository
import com.mhappening.gameclock.model.AppTheme
import com.mhappening.gameclock.model.ClockFormat
import com.mhappening.gameclock.model.ClockThemePreferences
import com.mhappening.gameclock.ui.theme.ClockFont
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

//    private var themesPreferencesList = mutableListOf<ClockThemePreferences>()

    init {
        loadLastUsedTheme()
        firstLoad()
        showButtons()
    }

    private fun firstLoad() {
        viewModelScope.launch {
            if (clockThemePreferencesRepository.getAllClockThemePreferences().firstOrNull().isNullOrEmpty()) {
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
    }

    private fun loadLastUsedTheme() {
        viewModelScope.launch {
            val lastUsedThemeName = userPreferencesRepository.lastOpenedTheme.firstOrNull()
            if (lastUsedThemeName != null) {
                val lastUsedThemePreferences =
                    clockThemePreferencesRepository.getClockThemePreferences(
                        AppTheme.valueOf(lastUsedThemeName)
                    ).firstOrNull()
                if (lastUsedThemePreferences != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            theme = lastUsedThemePreferences.appTheme,
                            showAnimations = lastUsedThemePreferences.showAnimations,
                            clockFormat = lastUsedThemePreferences.clockFormat,
                            clockScale = lastUsedThemePreferences.clockScale,
                            buttonsScale = lastUsedThemePreferences.buttonsScale,
                            showAlarmButton = lastUsedThemePreferences.showAlarmButton,
                            showTimerButton = lastUsedThemePreferences.showTimerButton,
                            clockFont = lastUsedThemePreferences.clockFont
                        )
                    }
                }
            }
        }
    }

    fun onThemeChange(event: ThemeChangeEvent) {
        when (event) {
            is ThemeChangeEvent.ThemeChange -> {
                viewModelScope.launch {
                    val newThemePreferences =
                        clockThemePreferencesRepository.getClockThemePreferences(event.theme)
                            .firstOrNull()
                    if (newThemePreferences != null) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                theme = newThemePreferences.appTheme,
                                showAnimations = newThemePreferences.showAnimations,
                                clockFormat = newThemePreferences.clockFormat,
                                clockScale = newThemePreferences.clockScale,
                                buttonsScale = newThemePreferences.buttonsScale,
                                showAlarmButton = newThemePreferences.showAlarmButton,
                                showTimerButton = newThemePreferences.showTimerButton,
                                clockFont = newThemePreferences.clockFont
                            )
                        }
                        userPreferencesRepository.setLastOpenedTheme(event.theme.themeName)
                    } else {
                        // In the event of a theme not in the repository, check clockthemeList and add it to the repository
                        val newTheme = ClockThemeList().getThemeByAppTheme(event.theme)
                        clockThemePreferencesRepository.writeClockThemePreferences(newTheme)
                        onThemeChange(event)

                    }
                }
            }
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
                    thumbnail = ClockThemeList().getThemeByAppTheme(clockUiState.value.theme).thumbnail
//                    themesPreferencesList.first { it.appTheme == clockUiState.value.theme }.thumbnail

                )
            )
        }
    }


    fun resetThemeToDefaults() {
        viewModelScope.launch {
            val themeDefaults = ClockThemeList().loadThemes()
                .first { it.appTheme == _uiState.value.theme }
            _uiState.update { ui ->
                ui.copy(
                    showAnimations = themeDefaults.showAnimations,
                    clockFormat = themeDefaults.clockFormat,
                    clockScale = themeDefaults.clockScale,
                    buttonsScale = themeDefaults.buttonsScale,
                    showAlarmButton = themeDefaults.showAlarmButton,
                    showTimerButton = themeDefaults.showTimerButton,
                    clockFont = themeDefaults.clockFont

                )
            }
        }
    }


    // Create a job to hold the coroutine that hides the buttons
    private var hideButtonsJob: Job? = null


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
    val clockFormat: ClockFormat = ClockFormat.VERTICAL_TWELVE_HOUR,
    val isFullScreen: Boolean = false,
    val clockScale: Float = 1.8f,
    val buttonsScale: Float = 1.2f,
    val showAlarmButton: Boolean = true,
    val showTimerButton: Boolean = false,
    val buttonsVisible: Boolean = true,
    val clockFont: ClockFont = ClockFont.GERMANIA_ONE

)

