package com.alperenturker.myapplication.presentation.favorites.views


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alperenturker.domain.model.Movie
import com.alperenturker.myapplication.presentation.favorites.FavoritesViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.alperenturker.core.ui.components.BottomFadeOverlay
import com.alperenturker.core.ui.components.PosterTile
import com.alperenturker.myapplication.presentation.ui.theme.Dark


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
                    PosterTile(
                        imageUrl = movie.Poster,
                        contentDescription = movie.Title,
                        modifier = Modifier.width(150.dp),
                        onClick = { onItemClick(movie) },
                        overlay = {
                            // alt gradyan
                            BottomFadeOverlay(startY = 120f)

                            // alt metinler (title + year)
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

                            // sağ üst favori rozeti
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
                    )
                }
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