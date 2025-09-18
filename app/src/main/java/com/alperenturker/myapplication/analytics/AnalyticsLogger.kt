package com.alperenturker.myapplication.analytics

interface AnalyticsLogger {
    fun logScreen(name: String, params: Map<String, Any?> = emptyMap())
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap())
}