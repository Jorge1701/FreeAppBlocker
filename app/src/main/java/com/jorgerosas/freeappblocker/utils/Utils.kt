package com.jorgerosas.freeappblocker.utils

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.jorgerosas.freeappblocker.utils.Constants.TAG

fun Context.getForegroundApp(): String? {
    val usm = this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val now = System.currentTimeMillis()

    // Query usage stats for the last 30 second
    val stats: List<UsageStats> = usm.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        now - 30_000,
        now
    )

    if (stats.isEmpty()) {
        Log.d(TAG, "No usage stats available - permission missing?")
        return null
    }

    // Find the app with the latest lastTimeUsed
    val currentApp = stats.maxByOrNull { it.lastTimeUsed }?.packageName

    return currentApp
}
