package com.alperenturker.myapplication.presentation.movies.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.alperenturker.myapplication.domain.model.Movie

@Composable
fun MovieListRow(
    movie: Movie,
    onItemClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(movie) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.Poster)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.Title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(96.dp)            // sabit genişlik
                    .aspectRatio(2f / 3f)    // poster oranı
                    .clip(RoundedCornerShape(12.dp)),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )

            Spacer(Modifier.width(12.dp))

            // Başlık + Yıl
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.Title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                AssistChip(
                    onClick = { /* no-op */ },
                    label = { Text(text = movie.Year) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null
                        )
                    }
                )
            }

            Spacer(Modifier.width(8.dp))

            // Sağ ok (navigasyon ipucu)
            Icon(
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
