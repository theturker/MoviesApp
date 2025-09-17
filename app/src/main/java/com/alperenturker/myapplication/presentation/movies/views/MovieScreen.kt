package com.alperenturker.myapplication.presentation.movies.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.alperenturker.myapplication.presentation.Screen
import com.alperenturker.myapplication.presentation.movies.MoviesEvent
import com.alperenturker.myapplication.presentation.movies.MoviesViewModel
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.Color
import com.alperenturker.domain.model.Movie

private object Dark {
    val Bg = Color(0xFF000000)
    val Surface = Color(0xFF0E0E0E)
    val Surface2 = Color(0xFF161616)
    val OnBg = Color(0xFFEDEDED)
    val OnDim = Color(0xFFB5B5B5)
    val Outline = Color(0xFF2C2C2C)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var query by remember { mutableStateOf("") }
    val gridState = rememberLazyGridState()
    val movies = remember(state.movies) { state.movies.distinctBy { it.imdbID } }

    Scaffold(
        containerColor = Dark.Bg,
        contentColor = Dark.OnBg,
        topBar = {
            LargeTopAppBar(
                title = { Text("Discover", color = Dark.OnBg) },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Dark.Bg,
                    titleContentColor = Dark.OnBg
                ),
                actions = {
                    IconButton(
                        onClick = {
                            // Kendi route'unu kullan:
                            // Screen.FavoritesScreen.route (veya Screen.BookmarksScreen.route)
                            navController.navigate(Screen.FavoritesScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favoriler",
                            tint = Dark.OnBg
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Dark.Bg)
                .padding(padding)
        ) {
            // Search
            SearchField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.onEvent(MoviesEvent.Search(it))
                },
                onClear = {
                    query = ""
                    viewModel.onEvent(MoviesEvent.Search(""))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(Modifier.height(4.dp))

            // Grid 3 sütun poster
            if (movies.isEmpty() && !state.isLoading) {
                EmptyView(
                    text = if (query.isBlank()) "Popüler filmleri aratın." else "Sonuç bulunamadı."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    state = gridState,
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = movies,
                        key = { it.imdbID }
                    ) { movie ->
                        MoviePosterCard(
                            movie = movie,
                            onClick = {
                                navController.navigate(
                                    Screen.MovieDetailScreen.route + "/${movie.imdbID}"
                                )
                            }
                        )
                    }

                    // Yüklenirken 9 adet shimmer
                    if (state.isLoading && movies.isEmpty()) {
                        items(9) {
                            PosterSkeleton()
                        }
                    }
                }
            }
        }

        // Orta ekranda yükleme (liste doluysa göstermiyoruz)
        if (state.isLoading && movies.isNotEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Dark.OnBg, trackColor = Dark.Surface2)
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Ara: Batman, Inception...", color = Dark.OnDim) },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Dark.OnDim) },
        trailingIcon = {
            if (value.isNotBlank()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Outlined.Clear, contentDescription = "Temizle", tint = Dark.OnDim)
                }
            }
        },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { /* klavyeden ara */ }
        ),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Dark.Surface2,
            unfocusedContainerColor = Dark.Surface2,
            disabledContainerColor = Dark.Surface2,
            focusedBorderColor = Dark.Outline,
            unfocusedBorderColor = Dark.Outline,
            cursorColor = Dark.OnBg,
            focusedTextColor = Dark.OnBg,
            unfocusedTextColor = Dark.OnBg
        ),
        modifier = modifier
    )
}

@Composable
private fun MoviePosterCard(
    movie: Movie,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    ElevatedCard(
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = Dark.Surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Sadece poster – 2:3 oran
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.Poster)
                .crossfade(true)
                .build(),
            contentDescription = movie.Title,
            contentScale = ContentScale.Crop,
            loading = { PosterSkeleton() },
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(shape)
        )
    }
}

@Composable
private fun PosterSkeleton() {
    val base = Dark.Surface2
    val highlight = Color(0xFF232323)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val xOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = "shimmerX"
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .aspectRatio(2f / 3f)
            .background(
                Brush.linearGradient(
                    colors = listOf(base, highlight, base),
                    start = Offset(xOffset - 600f, 0f),
                    end = Offset(xOffset, 0f)
                )
            )
    )
}

@Composable
private fun EmptyView(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Dark.OnDim
        )
    }
}
