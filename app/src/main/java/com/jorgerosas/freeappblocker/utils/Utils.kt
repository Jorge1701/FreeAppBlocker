package com.jorgerosas.freeappblocker.utils

import android.content.Context
import android.content.Intent
import com.jorgerosas.freeappblocker.views.BlockingScreenView

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
