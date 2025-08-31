package com.jorgerosas.freeappblocker.entity

import java.time.DayOfWeek
import java.time.LocalTime

data class PackageSettings(
    val name: String,
    val sessionLimitRule: SessionLimitRuleConfig? = null,
    val timeRestrictionsRule: TimeRestrictionsRuleConfig? = null,
    val dailyUsageRule: DailyUsageRuleConfig? = null,
)

enum class RuleType {
    SESSION_LIMIT,
    TIME_RESTRICTION,
    DAILY_USAGE,
}

data class SessionLimitRuleConfig(
    val maxSessionMs: Long,
    val blockMs: Long = -1,
)

data class TimeRestrictionsRuleConfig(
    val restrictions: List<TimeRestriction>,
    val extension: RuleExtensionConfig? = null,
)

data class TimeRestriction(
    val day: DayOfWeek,
    val start: LocalTime,
    val end: LocalTime,
)

data class DailyUsageRuleConfig(
    val dailyLimitMs: Long,
    val extension: RuleExtensionConfig? = null,
)

data class RuleExtensionConfig(
    val amount: Int,
    val extensionTimeMs: Long,
)
