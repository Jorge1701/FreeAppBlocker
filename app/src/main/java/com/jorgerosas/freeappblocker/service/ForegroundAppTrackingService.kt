package com.jorgerosas.freeappblocker.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.jorgerosas.freeappblocker.utils.Constants.FIXED_LIMIT_MS
import com.jorgerosas.freeappblocker.utils.Constants.TAG
import com.jorgerosas.freeappblocker.utils.Constants.USAGE_CHECK_MS
import com.jorgerosas.freeappblocker.utils.getCurrentPackage
import com.jorgerosas.freeappblocker.utils.goHome

class ForegroundAppTrackingService : AccessibilityService() {
    private var currentPackage: String? = null
    private var startTimeMs: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private val timerTask = object : Runnable {
        override fun run() {
            this@ForegroundAppTrackingService.currentPackage?.let {
                val currentPackageUseTimeMs = System.currentTimeMillis() - startTimeMs
                Log.d(TAG, "USE $it - ${currentPackageUseTimeMs / 1000}s")

                if (currentPackageUseTimeMs >= FIXED_LIMIT_MS) {
                    Log.d(TAG, "BLOCK $it")
                    this@ForegroundAppTrackingService.goHome()
                    return
                }

                // Schedule next tick in 1 minute
                handler.postDelayed(this, USAGE_CHECK_MS)
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val newPackage = baseContext.getCurrentPackage() ?: return

            if (newPackage != currentPackage) {
                if (!currentPackage.isNullOrEmpty()) {
                    Log.d(TAG, "CLOSED ${this.currentPackage}");
                }

                Log.d(TAG, "OPENED $newPackage");

                startTimeMs = System.currentTimeMillis()
                currentPackage = newPackage;

                handler.removeCallbacks(timerTask)
                handler.postDelayed(timerTask, USAGE_CHECK_MS)
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
