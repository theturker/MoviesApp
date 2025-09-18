package com.alperenturker.myapplication.presentation.navigation

import androidx.lifecycle.ViewModel
import com.alperenturker.myapplication.analytics.AnalyticsLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsHostViewModel @Inject constructor(
    val logger: AnalyticsLogger
) : ViewModel()
