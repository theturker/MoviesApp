package com.alperenturker.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun PosterSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(14.dp),
    baseColor: Color = Dark.Surface2,
    highlightColor: Color = Color(0xFF232323),
    shimmerDurationMs: Int = 1200
) {
    val transition = rememberInfiniteTransition(label = "poster_shimmer")
    val x by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(animation = tween(shimmerDurationMs, easing = LinearEasing)),
        label = "poster_shimmer_x"
    )
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = listOf(baseColor, highlightColor, baseColor),
                    start = Offset(x - 600f, 0f),
                    end = Offset(x, 0f)
                )
            )
    )
}
