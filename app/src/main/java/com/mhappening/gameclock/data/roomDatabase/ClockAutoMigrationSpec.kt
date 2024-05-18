package com.mhappening.gameclock.data.roomDatabase

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec


@RenameColumn.Entries(
    RenameColumn(
        tableName = "alarm_table",
        fromColumnName = "id",
        toColumnName = "alarm_id"
    ),
    RenameColumn(
        tableName = "alarm_table",
        fromColumnName = "title",
        toColumnName = "alarm_title"
    ),
    RenameColumn(
        tableName = "alarm_table",
        fromColumnName = "isEnabled",
        toColumnName = "is_alarm_enabled"
    ),
    RenameColumn(
        tableName = "alarm_table",
        fromColumnName = "date",
        toColumnName = "alarm_date"
    )
)
class ClockAutoMigrationSpec : AutoMigrationSpec
