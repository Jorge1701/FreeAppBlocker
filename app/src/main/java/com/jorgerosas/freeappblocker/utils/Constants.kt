package com.jorgerosas.freeappblocker.utils

import com.jorgerosas.freeappblocker.entity.PackageSettings
import com.jorgerosas.freeappblocker.entity.SessionLimitRule

object Constants {
    const val TAG = "MY_EVENTS"
    const val QUERY_STATS_INTERVAL_MS = 30_000
    const val USAGE_CHECK_MS = 1000L

    // TODO Save to persistent config
    val APPS_CONFIG = mapOf(
        "com.whatsapp" to PackageSettings(
            name = "com.whatsapp",
            sessionLimitRule = SessionLimitRule(
                maxSessionMs = 3000,
            )
        ),
        "com.instagram.android" to PackageSettings(
            name = "com.instagram.android",
            sessionLimitRule = SessionLimitRule(
                maxSessionMs = 1000,
            )
        ),
        "com.android.chrome" to PackageSettings(
            name = "com.android.chrome",
            sessionLimitRule = SessionLimitRule(
                maxSessionMs = 10000,
                blockMs = 10000,
            )
        ),
    )
}
