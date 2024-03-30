package com.example.gameclock.ui.screens.backgrounds.pixel_background_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PixelBackgroundViewModel : ViewModel() {

    private val _state = MutableStateFlow(PixelBackgroundState())
    val state: StateFlow<PixelBackgroundState> = _state

    fun updatePixels(pixels: List<Int>) {
        _state.value = PixelBackgroundState(pixels)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PixelBackgroundViewModel()
            }
        }
    }
}

data class PixelBackgroundState(var pixels: List<Int> = emptyList())
