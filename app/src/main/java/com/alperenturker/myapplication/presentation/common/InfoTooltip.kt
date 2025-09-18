package com.alperenturker.myapplication.presentation.common



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTooltip(
    text: String,
    modifier: Modifier = Modifier,
    tint: Color = Color(0xFFB5B5B5) // Dark.OnDim gibi
) {
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState(isPersistent = true)

    TooltipBox(
        state = tooltipState,
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(text)
            }
        },
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    if (tooltipState.isVisible) tooltipState.dismiss() else tooltipState.show()
                }
            }
        ) {
            Icon(Icons.Outlined.Info, contentDescription = "Bilgi", tint = tint)
        }
    }
}
