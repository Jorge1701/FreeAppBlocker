package com.jorgerosas.freeappblocker.utils

object Constants {
    const val TAG = "MY_EVENTS"
    const val QUERY_STATS_INTERVAL_MS = 30_000
    const val USAGE_CHECK_MS = 1000L

    // TODO Change for app specific configuration
    const val FIXED_LIMIT_MS = 3_000
    // TODO Change for user selection
    val PACKAGES_TO_CHECK = listOf(
        "com.whatsapp"
    )
}
