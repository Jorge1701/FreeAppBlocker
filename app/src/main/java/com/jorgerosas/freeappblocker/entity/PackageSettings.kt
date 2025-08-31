package com.jorgerosas.freeappblocker.entity

import java.time.DayOfWeek
import java.time.LocalTime

data class PackageSettings(
    val name: String,
    val sessionLimitRule: SessionLimitRule? = null,
    val timeRestrictionsRule: TimeRestrictionsRule? = null,
    val dailyUsageRule: DailyUsageRule? = null,
)

data class SessionLimitRule(
    val maxSessionMs: Long,
    val blockMs: Long = -1,
)

data class TimeRestrictionsRule(
    val restrictions: List<TimeRestriction>
)

data class TimeRestriction(
    val day: DayOfWeek,
    val start: LocalTime,
    val end: LocalTime,
)

data class DailyUsageRule(
    val dailyLimitMs: Long,
)
