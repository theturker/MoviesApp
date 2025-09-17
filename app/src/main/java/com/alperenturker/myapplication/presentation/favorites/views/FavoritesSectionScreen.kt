package com.alperenturker.myapplication.presentation.favorites.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.alperenturker.domain.model.Movie
import com.alperenturker.myapplication.presentation.favorites.FavoritesViewModel
import androidx.hilt.navigation.compose.hiltViewModel

/* ——— Tema (Movie/Detail ekranlarındakiyle uyumlu) ——— */
private object Dark {
    val Bg = Color(0xFF000000)
    val Surface = Color(0xFF0E0E0E)
    val Surface2 = Color(0xFF161616)
    val OnBg = Color(0xFFEDEDED)
    val OnDim = Color(0xFFB5B5B5)
    val Outline = Color(0xFF2C2C2C)
}

/* ——— Ekran ——— */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesSectionScreen(
    onItemClick: (Movie) -> Unit,
    vm: FavoritesViewModel = hiltViewModel()
) {
    val items by vm.favorites.collectAsState()

    Scaffold(
        containerColor = Dark.Bg,
        contentColor = Dark.OnBg,
        topBar = {
            TopAppBar(
                title = { Text("Favorites", color = Dark.OnBg) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Dark.Bg,
                    titleContentColor = Dark.OnBg,
                    navigationIconContentColor = Dark.OnBg,
                    actionIconContentColor = Dark.OnBg
                )
            )
        }
    ) { padding ->
        if (items.isEmpty()) {
            EmptyFavorites(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .background(Dark.Bg)
            ) {
                items(items, key = { it.imdbID }) { movie ->
                    FavoritePosterCard(
                        movie = movie,
                        onClick = { onItemClick(movie) }
                    )
                }
            }
        }
    }
}

/* ——— Kart ——— */
@Composable
private fun FavoritePosterCard(
    movie: Movie,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    ElevatedCard(
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.elevatedCardColors(containerColor = Dark.Surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.width(150.dp)
    ) {
        Box(modifier = Modifier.clip(shape)) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.Poster)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.Title,
                contentScale = ContentScale.Crop,
                loading = { PosterSkeleton() },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
            )

            // Alt gradyan + metin
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
                    .padding(10.dp)
            ) {
                Text(
                    text = movie.Title,
                    style = MaterialTheme.typography.labelLarge,
                    color = Dark.OnBg,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = movie.Year,
                    style = MaterialTheme.typography.labelSmall,
                    color = Dark.OnDim
                )
            }

            // Üst-sağda küçük rozet
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color(0x66000000), RoundedCornerShape(999.dp))
                    .padding(6.dp)
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Dark.OnBg)
            }
        }
    }
}

/* ——— Boş durum ——— */
@Composable
private fun EmptyFavorites(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = Dark.OnDim,
            modifier = Modifier.size(42.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("Henüz favori yok", color = Dark.OnBg)
        Spacer(Modifier.height(4.dp))
        Text(
            "Detay sayfasındaki yıldız ile favorilere ekleyebilirsin.",
            color = Dark.OnDim,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

/* ——— Shimmer (Movie ekranındakiyle uyumlu) ——— */
@Composable
private fun PosterSkeleton() {
    val base = Dark.Surface2
    val highlight = Color(0xFF232323)
    val transition = rememberInfiniteTransition(label = "fav_shimmer")
    val x by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(animation = tween(1200, easing = LinearEasing)),
        label = "fav_shimmer_x"
    )
    Box(
        modifier = Modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(base, highlight, base),
                    start = Offset(x - 600f, 0f),
                    end = Offset(x, 0f)
                )
            )
    )
}
