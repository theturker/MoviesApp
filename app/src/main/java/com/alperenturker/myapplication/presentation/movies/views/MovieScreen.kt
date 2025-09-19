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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alperenturker.myapplication.presentation.Screen
import com.alperenturker.myapplication.presentation.movies.MoviesEvent
import com.alperenturker.myapplication.presentation.movies.MoviesViewModel
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MailOutline
import com.alperenturker.core.ui.components.BottomFadeOverlay
import com.alperenturker.core.ui.components.PosterSkeleton
import com.alperenturker.core.ui.components.PosterTile
import com.alperenturker.myapplication.presentation.ai.AiViewModel
import com.alperenturker.myapplication.presentation.ai.views.AiBottomSheet
import com.alperenturker.myapplication.presentation.ui.theme.Dark


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

    // AI
    val aiVm = hiltViewModel<AiViewModel>()
    var aiOpen by remember { mutableStateOf(false) }

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
                    // Favoriler
                    IconButton(
                        onClick = { navController.navigate(Screen.FavoritesScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favoriler",
                            tint = Dark.OnBg
                        )
                    }
                    // AI Öneri
                    IconButton(onClick = { aiOpen = true }) {
                        Icon(
                            imageVector = Icons.Outlined.MailOutline, // ya da TipsAndUpdates
                            contentDescription = "AI Öneri",
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

            // Grid
            if (movies.isEmpty() && !state.isLoading) {
                EmptyView(
                    text = if (query.isBlank()) "Popüler filmleri aratın." else "Sonuç bulunamadı."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp), // 2 sütun telefon, 3+ tablet
                    state = gridState,
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items = movies, key = { it.imdbID }) { movie ->
                        PosterTile(
                            imageUrl = movie.Poster,
                            contentDescription = movie.Title,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),            // biraz daha yumuşak köşe
                            onClick = {
                                navController.navigate(Screen.MovieDetailScreen.route + "/${movie.imdbID}")
                            },
                            overlay = {
                                // 1) alttan koyulaşan fade
                                BottomFadeOverlay(startY = 220f)

                                // 2) başlık + yıl (sol altta)
                                androidx.compose.foundation.layout.Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp)
                                ) {
                                    androidx.compose.material3.Text(
                                        text = movie.Title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Dark.OnBg,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    androidx.compose.foundation.layout.Spacer(Modifier.height(2.dp))
                                    androidx.compose.material3.Text(
                                        text = movie.Year ?: "",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Dark.OnDim
                                    )
                                }
                            }
                        )
                    }

                    if (state.isLoading && movies.isEmpty()) {
                        items(6) { // 2x3 iskelet
                            PosterSkeleton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(2f / 3f),
                                shape = RoundedCornerShape(18.dp)
                            )
                        }
                    }
                }
            }
        }

        if (state.isLoading && movies.isNotEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Dark.OnBg, trackColor = Dark.Surface2)
            }
        }

        /* --- AI BottomSheet --- */
        if (aiOpen) {
            AiBottomSheet(
                initialQuery = query,
                vm = aiVm,
                onDismiss = { aiOpen = false },
                onUseSuggestion = { title ->
                    // AI önerilen başlıkla mevcut listeyi arat
                    query = title
                    viewModel.onEvent(MoviesEvent.Search(title))
                    aiOpen = false
                }
            )
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
