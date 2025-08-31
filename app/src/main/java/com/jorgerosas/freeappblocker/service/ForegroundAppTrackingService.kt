package com.jorgerosas.freeappblocker.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.jorgerosas.freeappblocker.rules.Rules
import com.jorgerosas.freeappblocker.utils.Constants.TAG
import com.jorgerosas.freeappblocker.utils.Constants.USAGE_CHECK_MS
import com.jorgerosas.freeappblocker.utils.getCurrentPackage
import com.jorgerosas.freeappblocker.utils.showBlockingScreen

class ForegroundAppTrackingService : AccessibilityService() {
    private var currentPackage: String? = null
    private var startTimeMs: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private val timerTask = object : Runnable {
        override fun run() {
            this@ForegroundAppTrackingService.currentPackage?.let { packageName ->
                val currentPackageUseTimeMs = System.currentTimeMillis() - startTimeMs
                Log.d(TAG, "CHECK $packageName [current_session:$currentPackageUseTimeMs]")

                Rules.INSTANCE.checkCurrentPackageRules(
                    packageName = packageName,
                    sessionTimeMs = currentPackageUseTimeMs
                ).let { shouldBlock ->
                    if (shouldBlock) {
                        showBlockingScreen(
                            context = this@ForegroundAppTrackingService
                        )
                    } else {
                        // Schedule next check
                        handler.postDelayed(this, USAGE_CHECK_MS)
                    }
                }
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val newPackage = baseContext.getCurrentPackage() ?: return

            if (newPackage != currentPackage) {
                handler.removeCallbacks(timerTask)

                if (!currentPackage.isNullOrEmpty()) {
                    Log.d(TAG, "CLOSED ${this.currentPackage}");
                }

                Log.d(TAG, "OPENED $newPackage");

                if (Rules.INSTANCE.shouldBlockPackageFromOpening(newPackage)) {
                    showBlockingScreen(
                        context = this
                    )

                    return
                }

                startTimeMs = System.currentTimeMillis()
                currentPackage = newPackage

                if (Rules.INSTANCE.shouldCheckPackage(currentPackage!!)) {
                    handler.postDelayed(timerTask, USAGE_CHECK_MS)
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected");
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }
}
