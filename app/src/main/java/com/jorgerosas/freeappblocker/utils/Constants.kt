package com.jorgerosas.freeappblocker.utils

import com.jorgerosas.freeappblocker.entity.DailyUsageRuleConfig
import com.jorgerosas.freeappblocker.entity.PackageSettings
import com.jorgerosas.freeappblocker.entity.RuleExtensionConfig
import com.jorgerosas.freeappblocker.entity.SessionLimitRuleConfig
import com.jorgerosas.freeappblocker.entity.TimeRestriction
import com.jorgerosas.freeappblocker.entity.TimeRestrictionsRuleConfig
import java.time.DayOfWeek
import java.time.LocalTime

object Constants {
    const val TAG = "MY_EVENTS"
    const val QUERY_STATS_INTERVAL_MS = 30_000
    const val USAGE_CHECK_MS = 1000L

    // TODO Save to persistent config
    val APPS_CONFIG = mapOf(
        "com.whatsapp" to PackageSettings(
            name = "com.whatsapp",
            sessionLimitRule = SessionLimitRuleConfig(
                maxSessionMs = 3000,
            ),
            timeRestrictionsRule = TimeRestrictionsRuleConfig(
                restrictions = listOf(
                    TimeRestriction(
                        day = DayOfWeek.SUNDAY,
                        start = LocalTime.of(9, 52),
                        end = LocalTime.of(15, 0),
                    )
                ),
                extension = RuleExtensionConfig(
                    amount = 3,
                    extensionTimeMs = 10_000,
                )
            )
        ),
        "com.instagram.android" to PackageSettings(
            name = "com.instagram.android",
            dailyUsageRule = DailyUsageRuleConfig(
                dailyLimitMs = (26 * 60 * 1000).toLong(),
                extension = RuleExtensionConfig(
                    amount = 2,
                    extensionTimeMs = 5_000,
                )
            ),
        ),
        "com.android.chrome" to PackageSettings(
            name = "com.android.chrome",
            sessionLimitRule = SessionLimitRuleConfig(
                maxSessionMs = 10000,
                blockMs = 10000,
            ),
        ),
    )
}
