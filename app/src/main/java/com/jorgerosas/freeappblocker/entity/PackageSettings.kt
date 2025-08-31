package com.jorgerosas.freeappblocker.entity

data class PackageSettings(
    val name: String,
    val sessionLimitRule: SessionLimitRule?,
)

data class SessionLimitRule(
    val maxSessionMs: Long,
    val blockMs: Long = -1,
)
