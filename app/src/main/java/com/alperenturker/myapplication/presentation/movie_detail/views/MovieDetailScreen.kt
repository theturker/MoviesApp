package com.alperenturker.myapplication.presentation.movie_detail.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.alperenturker.myapplication.presentation.movie_detail.MovieDetailViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.outlined.AddCircle

private object Dark {
    val Bg = Color(0xFF000000)
    val Surface = Color(0xFF0E0E0E)
    val Surface2 = Color(0xFF161616)
    val OnBg = Color(0xFFEDEDED)
    val OnDim = Color(0xFFB5B5B5)
    val Outline = Color(0xFF2C2C2C)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieDetailScreen(
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null
) {
    val state = movieDetailViewModel.state.value
    val title = state.movie?.Title ?: "Movie detail"

    Scaffold(
        containerColor = Dark.Bg,
        contentColor = Dark.OnBg,
        topBar = {
            TopAppBar(
                title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Dark.OnBg) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = Dark.OnBg
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Dark.Bg,
                    titleContentColor = Dark.OnBg,
                    navigationIconContentColor = Dark.OnBg,
                    actionIconContentColor = Dark.OnBg
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Dark.Bg)
                .padding(padding)
        ) {
            state.movie?.let { m ->
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    // HERO
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(m.Poster)
                                .crossfade(true)
                                .build(),
                            contentDescription = m.Title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Koyu gradyan: üst transparan, alta doğru siyaha ve arka plana
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0x99000000),
                                            Dark.Bg
                                        ),
                                        startY = 120f
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = m.Title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Dark.OnBg,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Pill(Icons.Outlined.DateRange, m.Year.takeIfUseful())
                                Pill(Icons.Outlined.Star, m.imdbRating.takeIfUseful()?.let { "$it / 10 IMDb" })
                                Pill(Icons.Outlined.Person, m.Country.takeIfUseful())
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    ElevatedCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(containerColor = Dark.Surface),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            LabeledValue("Review", m.Plot.takeIfUseful(), Icons.Outlined.AddCircle)
                            LabeledValue("Director", m.Director.takeIfUseful(), Icons.Outlined.Person)
                            LabeledValue("Actors", m.Actors.takeIfUseful(), Icons.Outlined.Person)
                            LabeledValue("Country", m.Country.takeIfUseful(), Icons.Outlined.Person)
                            LabeledValue("Year", m.Year.takeIfUseful(), Icons.Outlined.DateRange)
                        }
                    }

                    Spacer(Modifier.height(28.dp))
                }
            }

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Dark.OnBg, trackColor = Dark.Surface2)
                }
            }
        }
    }
}

/* ---------- Yardımcılar ---------- */

@Composable
private fun Pill(icon: ImageVector, text: String?) {
    if (text.isNullOrBlank()) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Dark.Surface2, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Dark.OnDim)
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Dark.OnBg
        )
    }
}

@Composable
private fun LabeledValue(label: String, value: String?, icon: ImageVector) {
    if (value.isNullOrBlank()) return
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Dark.OnDim)
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Dark.OnDim
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Dark.OnBg
        )
    }
}

private fun String?.takeIfUseful(): String? =
    this?.takeIf { it.isNotBlank() && it != "N/A" }
