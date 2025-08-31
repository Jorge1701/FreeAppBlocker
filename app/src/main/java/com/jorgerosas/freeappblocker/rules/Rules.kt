package com.jorgerosas.freeappblocker.rules

import android.util.Log
import com.jorgerosas.freeappblocker.entity.APPS
import com.jorgerosas.freeappblocker.utils.Constants.TAG

class Rules private constructor() {
    companion object {
        val INSTANCE = Rules()
    }

    private val lastBlockedTimesMs = mutableMapOf<String, Long>()

    fun shouldCheckPackage(packageName: String) = APPS.keys.contains(packageName)

    fun shouldBlockPackageFromOpening(packageName: String): Boolean {
        val packageSettings = APPS[packageName]

        packageSettings?.sessionLimitRule?.let { sessionLimitRule ->
            lastBlockedTimesMs[packageName]?.let { lastBlockedTimeMs ->
                val elapsedMs = System.currentTimeMillis() - lastBlockedTimeMs
                if (elapsedMs < sessionLimitRule.blockMs) {
                    Log.d(
                        TAG,
                        "BLOCK OPEN $packageName - session rule [elapsed:$elapsedMs][blocked:${sessionLimitRule.blockMs}}"
                    )
                    return true
                }
            }
        }

        return false
    }

    fun checkCurrentPackageRules(
        packageName: String,
        startTimeMs: Long,
        shouldBlock: (Boolean) -> Unit,
    ) {
        Log.d(TAG, "CHECK $packageName")
        var result = false

        APPS[packageName]?.sessionLimitRule?.let { sessionLimitRule ->
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

        shouldBlock(result)
    }
}
