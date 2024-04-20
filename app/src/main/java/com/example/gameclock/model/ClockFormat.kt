package com.example.gameclock.model

enum class ClockFormat (
    val formatTitle: String,
    val formatValue: String,
    val timeSuffix: String,
) {
    TWELVE_HOUR("12 Hour", "hh:mm", "a"),
    TWENTY_FOUR_HOUR("24 Hour", "HH:mm", ""),
    TWELVE_HOUR_WITH_SECONDS("12 Hour with Seconds", "hh:mm:ss", "a"),
    TWENTY_FOUR_HOUR_WITH_SECONDS("24 Hour with Seconds", "HH:mm:ss", ""),
    VERTICAL_TWELVE_HOUR("Vertical 12 Hour", "hh\nmm", "a"),
    VERTICAL_TWENTY_FOUR_HOUR("Vertical 24 Hour", "HH\nmm", ""),
    VERTICAL_TWELVE_HOUR_WITH_SECONDS("Vertical 12 Hour with Seconds", "hh\nmm\nss", "a"),
    VERTICAL_TWENTY_FOUR_HOUR_WITH_SECONDS("Vertical 24 Hour with Seconds", "HH\nmm\nss", ""),

}
