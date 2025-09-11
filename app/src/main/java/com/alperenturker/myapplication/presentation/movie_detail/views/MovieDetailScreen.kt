package com.alperenturker.myapplication.presentation.movie_detail.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null
) {
    val state = movieDetailViewModel.state.value
    val title = state.movie?.Title ?: "Movie detail"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            state.movie?.let { m ->
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    // HERO: Poster + gradient + başlık/yıl
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(m.Poster)
                                .crossfade(true)
                                .build(),
                            contentDescription = m.Title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                )
                            },
                            error = {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        )

                        // Alt kısma doğru karartan gradient
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.35f),
                                            MaterialTheme.colorScheme.background
                                        ),
                                        startY = 80f
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
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(Modifier.height(8.dp))

                            AssistChip(
                                onClick = { /* no-op */ },
                                label = { Text(m.Year) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.DateRange,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Hızlı bilgiler (IMDb, Ülke, Yönetmen)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoPill(Icons.Outlined.Star, m.imdbRating.takeIfUseful()?.let { "$it / 10 IMDb" })
                        InfoPill(Icons.Outlined.Person, m.Director.takeIfUseful())
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoPill(Icons.Outlined.AddCircle, m.Country.takeIfUseful())
                    }

                    Spacer(Modifier.height(16.dp))

                    // Detaylar kartı
                    ElevatedCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            LabeledValue("Director", m.Director.takeIfUseful())
                            LabeledValue("Actors", m.Actors.takeIfUseful())
                            LabeledValue("Country", m.Country.takeIfUseful())
                            LabeledValue("Year", m.Year.takeIfUseful())
                            // Domain modelinde varsa buraya Plot/Genre/Runtime gibi alanları da ekleyebilirsin.
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }

            // Loading overlay (movie null iken de gösterilsin diye dışarı aldık)
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

/* ---------- Küçük yardımcılar ---------- */

@Composable
private fun InfoPill(icon: ImageVector, text: String?) {
    if (text.isNullOrBlank()) return
    AssistChip(
        onClick = { /* no-op */ },
        label = { Text(text) },
        leadingIcon = { Icon(icon, contentDescription = null) }
    )
}

@Composable
private fun LabeledValue(label: String, value: String?) {
    if (value.isNullOrBlank()) return
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/** "N/A" veya boş-string'leri otomatik ele */
private fun String?.takeIfUseful(): String? =
    this?.takeIf { it.isNotBlank() && it != "N/A" }
