package com.jorgerosas.freeappblocker.views

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import com.jorgerosas.freeappblocker.R

class BlockingScreenView : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        setContentView(R.layout.overlay_block)
    }
}
