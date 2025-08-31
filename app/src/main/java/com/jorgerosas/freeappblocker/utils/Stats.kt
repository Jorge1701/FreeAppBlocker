package com.jorgerosas.freeappblocker.utils

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import com.jorgerosas.freeappblocker.utils.Constants.QUERY_STATS_INTERVAL_MS
import com.jorgerosas.freeappblocker.utils.Constants.TAG

class Stats private constructor() {
    companion object {
        val INSTANCE = Stats()
    }

    fun getTodayUsageInMsFor(context: Context, packageName: String): Long {
        return queryStats(
            context,
            beginTime = startOfToday()
        ).find { it.packageName == packageName }
            ?.totalTimeInForeground
            ?: 0
    }

    fun getPackageBeingUsed(context: Context): String? {
        val stats = queryStats(
            context,
            beginTime = System.currentTimeMillis() - QUERY_STATS_INTERVAL_MS
        )

        if (stats.isEmpty()) {
            Log.d(TAG, "No usage stats available - permission missing?")
            return null
        }

        // Find the package with the latest lastTimeUsed
        return stats.maxByOrNull { it.lastTimeUsed }?.packageName
    }

    private fun queryStats(context: Context, beginTime: Long): List<UsageStats> {
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val now = System.currentTimeMillis()

        return usm.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            beginTime,
            now
        )
    }

    private fun startOfToday() =
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
}
