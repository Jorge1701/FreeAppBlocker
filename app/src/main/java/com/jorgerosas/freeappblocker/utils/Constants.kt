package com.jorgerosas.freeappblocker.utils

import com.jorgerosas.freeappblocker.entity.PackageSettings
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
        "com.instagram.android" to PackageSettings(
            name = "com.instagram.android",
            sessionLimitRule = SessionLimitRuleConfig(
                maxSessionMs = 5 * 60 * 1000,
                blockMs = 15 * 60 * 1000,
            ),
            timeRestrictionsRule = TimeRestrictionsRuleConfig(
                restrictions = listOf(
                    TimeRestriction(
                        day = DayOfWeek.MONDAY,
                        start = LocalTime.of(9, 0),
                        end = LocalTime.of(13, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.MONDAY,
                        start = LocalTime.of(14, 0),
                        end = LocalTime.of(18, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.TUESDAY,
                        start = LocalTime.of(9, 0),
                        end = LocalTime.of(13, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.TUESDAY,
                        start = LocalTime.of(14, 0),
                        end = LocalTime.of(18, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.WEDNESDAY,
                        start = LocalTime.of(9, 0),
                        end = LocalTime.of(13, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.WEDNESDAY,
                        start = LocalTime.of(14, 0),
                        end = LocalTime.of(18, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.THURSDAY,
                        start = LocalTime.of(9, 0),
                        end = LocalTime.of(13, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.THURSDAY,
                        start = LocalTime.of(14, 0),
                        end = LocalTime.of(18, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.FRIDAY,
                        start = LocalTime.of(9, 0),
                        end = LocalTime.of(13, 0)
                    ),
                    TimeRestriction(
                        day = DayOfWeek.FRIDAY,
                        start = LocalTime.of(14, 0),
                        end = LocalTime.of(18, 0)
                    ),
                ),
            ),
        ),
    )
}
