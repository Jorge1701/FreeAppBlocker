package com.jorgerosas.freeappblocker.entity

data class RegisteredExtension(
    val activationTimeMs: Long,
    val rule: RuleType,
    val packageName: String,
)
