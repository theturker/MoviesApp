package com.alperenturker.core.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun BottomFadeOverlay(
    modifier: Modifier = Modifier,
    startY: Float = 120f,
    colors: List<Color> = listOf(Color.Transparent, Color(0x99000000), Dark.Bg)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = colors,
                    startY = startY
                )
            )
    )
}
object Dark {
    val Bg       = Color(0xFF000000)
    val Surface  = Color(0xFF0E0E0E)
    val Surface2 = Color(0xFF161616)
    val OnBg     = Color(0xFFEDEDED)
    val OnDim    = Color(0xFFB5B5B5)
    val Outline  = Color(0xFF2C2C2C)
}