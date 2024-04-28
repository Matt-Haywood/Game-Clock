package com.example.gameclock.ui.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaPlayerHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {

    private var mediaPlayer: MediaPlayer? = null

    fun prepare() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
            )
            isLooping = true
            setDataSource(applicationContext, Settings.System.DEFAULT_ALARM_ALERT_URI)
            prepare()
        }
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
