package com.jorgerosas.freeappblocker.utils

import com.jorgerosas.freeappblocker.entity.RegisteredExtension
import com.jorgerosas.freeappblocker.entity.RuleType

class Extensions private constructor() {
    companion object {
        val INSTANCE = Extensions()
    }

    private val CONSUMED_EXTENSIONS = mutableListOf<RegisteredExtension>()

    fun consumeExtension(packageName: String, rule: RuleType) {
        CONSUMED_EXTENSIONS.add(
            RegisteredExtension(
                activationTimeMs = System.currentTimeMillis(),
                rule, packageName
            )
        )
    }

    fun getConsumedExtensions(packageName: String, rule: RuleType): Int {
        val consumedExtensions = CONSUMED_EXTENSIONS
            .filter { it.packageName == packageName }
            .filter { it.rule == rule }

        return consumedExtensions.size
    }

    fun hasActiveExtension(
        packageName: String,
        rule: RuleType,
        configuredExtensionTimeMs: Long
    ): Boolean {
        return CONSUMED_EXTENSIONS
            .lastOrNull { it.packageName == packageName && it.rule == rule }
            ?.let { lastExtension ->
                val extensionActiveTimeMs =
                    System.currentTimeMillis() - lastExtension.activationTimeMs
                configuredExtensionTimeMs > extensionActiveTimeMs
            } ?: false
    }
}
