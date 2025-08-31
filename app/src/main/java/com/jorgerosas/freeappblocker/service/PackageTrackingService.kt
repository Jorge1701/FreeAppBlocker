package com.jorgerosas.freeappblocker.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.jorgerosas.freeappblocker.utils.Rules
import com.jorgerosas.freeappblocker.utils.Constants.TAG
import com.jorgerosas.freeappblocker.utils.Constants.USAGE_CHECK_MS
import com.jorgerosas.freeappblocker.utils.Stats
import com.jorgerosas.freeappblocker.utils.showBlockingScreen

class PackageTrackingService : AccessibilityService() {
    private var currentPackage: String? = null
    private var startTimeMs: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private val checkPackageTask = object : Runnable {
        override fun run() {
            this@PackageTrackingService.currentPackage?.let { packageName ->
                Rules.INSTANCE.checkPackageState(
                    context = this@PackageTrackingService,
                    packageName,
                    startTimeMs,
                ) { shouldBlock ->
                    if (shouldBlock) {
                        showBlockingScreen(
                            context = this@PackageTrackingService
                        )
                    } else {
                        handler.postDelayed(this, USAGE_CHECK_MS)
                    }
                }
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val newPackage = Stats.INSTANCE.getPackageBeingUsed(
                context = this
            ) ?: return

            if (newPackage != currentPackage) {
                Log.d(TAG, "OPENED $newPackage");

                handler.removeCallbacks(checkPackageTask)
                currentPackage = newPackage

                Rules.INSTANCE.checkPackageOpening(
                    context = this,
                    newPackage,
                ) { shouldBlock ->
                    if (shouldBlock) {
                        showBlockingScreen(
                            context = this
                        )
                    } else if (Rules.INSTANCE.shouldCheckPackage(newPackage)) {
                        startTimeMs = System.currentTimeMillis()
                        handler.postDelayed(checkPackageTask, USAGE_CHECK_MS)
                    }
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
