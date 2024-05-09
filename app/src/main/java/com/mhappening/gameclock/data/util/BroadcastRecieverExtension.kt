package com.mhappening.gameclock.data.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

fun Class<out BroadcastReceiver>?.setIntentAction(
    actionName: String,
    requestCode: Int,
    context: Context,
): PendingIntent {
    val broadcastIntent =
        Intent(context, this).apply {
            action = actionName
        }
    return PendingIntent.getBroadcast(
        context,
        requestCode,
        broadcastIntent,
        PendingIntent.FLAG_IMMUTABLE,
    )
}