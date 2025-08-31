package com.jorgerosas.freeappblocker.utils

import android.content.Context
import android.util.Log
import com.jorgerosas.freeappblocker.entity.RuleType
import com.jorgerosas.freeappblocker.entity.TimeRestriction
import com.jorgerosas.freeappblocker.entity.TimeRestrictionsRuleConfig
import com.jorgerosas.freeappblocker.utils.Constants.APPS_CONFIG
import com.jorgerosas.freeappblocker.utils.Constants.TAG
import java.time.LocalDateTime
import kotlin.random.Random

class Rules private constructor() {
    companion object {
        val INSTANCE = Rules()
    }

    private val lastBlockedTimesMs = mutableMapOf<String, Long>()

    fun shouldCheckPackage(packageName: String) = APPS_CONFIG.keys.contains(packageName)

    fun checkPackageOpening(
        context: Context,
        packageName: String,
        shouldBlock: (Boolean) -> Unit
    ) {
        Log.d(TAG, "CHECK OPEN $packageName")
        var result = false

        val config = APPS_CONFIG[packageName]

        config?.sessionLimitRule?.let { sessionLimitRule ->
            lastBlockedTimesMs[packageName]?.let { lastBlockedTimeMs ->
                val elapsedMs = System.currentTimeMillis() - lastBlockedTimeMs
                if (elapsedMs < sessionLimitRule.blockMs) {
                    Log.d(
                        TAG,
                        "BLOCK OPEN $packageName - session rule [elapsed:$elapsedMs][blocked:${sessionLimitRule.blockMs}}"
                    )
                    result = true
                }
            }
        }

        config?.timeRestrictionsRule?.let { timeRestrictionsRule ->
            findTimeRestriction(timeRestrictionsRule)?.let { timeRestriction ->
                Log.d(
                    TAG,
                    "BLOCK OPEN $packageName [day:${timeRestriction.day}][start:${timeRestriction.start}][end:${timeRestriction.end}]"
                )
                result = true
            }
        }

        config?.dailyUsageRule?.let { dailyUsageRule ->
            val todayUsageMs = Stats.INSTANCE.getLastReportedUsageMs(
                context,
                packageName,
            )

            if (todayUsageMs > dailyUsageRule.dailyLimitMs) {
                val extension = dailyUsageRule.extension
                if (extension == null) {
                    result = true
                } else {
                    val consumedExtensions = Extensions.INSTANCE.getConsumedExtensions(
                        packageName,
                        RuleType.DAILY_USAGE,
                    )

                    if (consumedExtensions >= extension.amount) {
                        result = true
                    } else {
                        val hasActiveExtension = Extensions.INSTANCE.hasActiveExtension(
                            packageName,
                            RuleType.DAILY_USAGE,
                            extension.extensionTimeMs,
                        )
                        if (!hasActiveExtension) {
                            if (Random.nextBoolean()) {
                                Log.d(TAG, "EXTENSION YES")
                                Extensions.INSTANCE.consumeExtension(
                                    packageName,
                                    RuleType.DAILY_USAGE,
                                )
                            } else {
                                Log.d(TAG, "EXTENSION NO")
                                result = true
                            }
                        }
                    }
                }
            }

            if (result) {
                Log.d(
                    TAG,
                    "BLOCK OPEN $packageName [today_usage:$todayUsageMs][daily_limit:${dailyUsageRule.dailyLimitMs}]"
                )
            }
        }

        shouldBlock(result)
    }

    fun checkPackageState(
        context: Context,
        packageName: String,
        startTimeMs: Long,
        shouldBlock: (Boolean) -> Unit,
    ) {
        Log.d(TAG, "CHECK CURRENT $packageName")
        var result = false

        val config = APPS_CONFIG[packageName]

        config?.sessionLimitRule?.let { sessionLimitRule ->
            val sessionTimeMs = System.currentTimeMillis() - startTimeMs
            if (sessionTimeMs > sessionLimitRule.maxSessionMs) {
                Log.d(
                    TAG,
                    "BLOCK USAGE $packageName [session_time:$sessionTimeMs][max_session:${sessionLimitRule.maxSessionMs}]"
                )
                lastBlockedTimesMs[packageName] = System.currentTimeMillis()
                result = true
            }
        }

        config?.timeRestrictionsRule?.let { timeRestrictionsRule ->
            findTimeRestriction(timeRestrictionsRule)?.let { timeRestriction ->
                Log.d(
                    TAG,
                    "BLOCK USAGE $packageName [day:${timeRestriction.day}][start:${timeRestriction.start}][end:${timeRestriction.end}]"
                )
                result = true
            }
        }

        config?.dailyUsageRule?.let { dailyUsageRule ->
            val sessionTimeMs = System.currentTimeMillis() - startTimeMs
            val todayUsageMs = Stats.INSTANCE.getLastReportedUsageMs(
                context,
                packageName,
            ) + sessionTimeMs

            if (todayUsageMs > dailyUsageRule.dailyLimitMs) {
                val extension = dailyUsageRule.extension
                if (extension == null) {
                    result = true
                } else {
                    val hasActiveExtension = Extensions.INSTANCE.hasActiveExtension(
                        packageName,
                        RuleType.DAILY_USAGE,
                        extension.extensionTimeMs,
                    )
                    if (!hasActiveExtension) {
                        result = true
                    }
                }
            }

            if (result) {
                Log.d(
                    TAG,
                    "BLOCK USAGE $packageName [today_usage:$todayUsageMs][daily_limit:${dailyUsageRule.dailyLimitMs}]"
                )
            }
        }

        shouldBlock(result)
    }

    private fun findTimeRestriction(rule: TimeRestrictionsRuleConfig): TimeRestriction? {
        val dateTime = LocalDateTime.now()

        val day = dateTime.dayOfWeek
        val time = dateTime.toLocalTime()

        return rule.restrictions
            .filter { it.day == day }
            .find { time.isAfter(it.start) && time.isBefore(it.end) }
    }
}
