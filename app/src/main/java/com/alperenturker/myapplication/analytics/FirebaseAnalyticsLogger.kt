package com.alperenturker.myapplication.analytics


import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsLogger @Inject constructor(
    private val fa: FirebaseAnalytics
) : AnalyticsLogger {

    override fun logScreen(name: String, params: Map<String, Any?>) {
        val bundle = params.toBundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
        }
        fa.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun logEvent(name: String, params: Map<String, Any?>) {
        fa.logEvent(name, params.toBundle())
    }
}

private fun Map<String, Any?>.toBundle(): Bundle = Bundle().also { b ->
    forEach { (k, v) ->
        when (v) {
            null -> Unit
            is String -> b.putString(k, v)
            is Int -> b.putInt(k, v)
            is Long -> b.putLong(k, v)
            is Double -> b.putDouble(k, v)
            is Float -> b.putFloat(k, v)
            is Boolean -> b.putBoolean(k, v)
            else -> b.putString(k, v.toString())
        }
    }
}
