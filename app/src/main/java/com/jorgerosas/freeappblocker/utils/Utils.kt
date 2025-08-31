package com.jorgerosas.freeappblocker.utils

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jorgerosas.freeappblocker.utils.Constants.QUERY_STATS_INTERVAL_MS
import com.jorgerosas.freeappblocker.utils.Constants.TAG
import com.jorgerosas.freeappblocker.views.BlockingScreenView

fun getCurrentPackage(context: Context): String? {
    val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val now = System.currentTimeMillis()

    // Query usage stats in interval
    val stats: List<UsageStats> = usm.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        now - QUERY_STATS_INTERVAL_MS,
        now
    )

    if (stats.isEmpty()) {
        Log.d(TAG, "No usage stats available - permission missing?")
        return null
    }

    // Find the package with the latest lastTimeUsed
    return stats.maxByOrNull { it.lastTimeUsed }?.packageName
}

fun showBlockingScreen(context: Context) {
    // Go to home
    context.startActivity(
        Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    )

    // Launch blocking screen
    context.startActivity(
        Intent(context, BlockingScreenView::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    )
}
