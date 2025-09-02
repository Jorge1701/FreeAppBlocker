package com.jorgerosas.freeappblocker.entity

import android.graphics.drawable.Drawable

data class App(
    val packageName: String,
    val name: String,
    val icon: Drawable,
)
