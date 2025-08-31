package com.jorgerosas.freeappblocker.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.jorgerosas.freeappblocker.utils.Constants.TAG
import com.jorgerosas.freeappblocker.utils.getForegroundApp

class ForegroundAppTrackingService : AccessibilityService() {
    private var lastPackage = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val foregroundApp = baseContext.getForegroundApp() ?: return

            if (foregroundApp != lastPackage) {
                if (lastPackage.isNotEmpty()) {
                    Log.d(TAG, "CLOSED $lastPackage");
                }
                if (foregroundApp.isNotEmpty()) {
                    Log.d(TAG, "OPENED $foregroundApp");
                }

                lastPackage = foregroundApp;
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
