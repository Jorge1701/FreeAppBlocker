package com.jorgerosas.freeappblocker.entity

data class PackageSettings(
    val name: String,
    val sessionLimitRule: SessionLimitRule?,
)

data class SessionLimitRule(
    val maxSessionMs: Long,
    val blockMs: Long = -1,
)

val APPS = mapOf(
    "com.whatsapp" to PackageSettings(
        name = "com.whatsapp",
        sessionLimitRule = SessionLimitRule(
            maxSessionMs = 3000,
        )
    ),
    "com.instagram.android" to PackageSettings(
        name = "com.instagram.android",
        sessionLimitRule = SessionLimitRule(
            maxSessionMs = 1000,
        )
    ),
    "com.android.chrome" to PackageSettings(
        name = "com.android.chrome",
        sessionLimitRule = SessionLimitRule(
            maxSessionMs = 10000,
            blockMs = 10000,
        )
    ),
)
