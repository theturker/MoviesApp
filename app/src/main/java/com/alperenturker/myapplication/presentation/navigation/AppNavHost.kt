package com.alperenturker.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alperenturker.myapplication.common.orElse
import com.alperenturker.myapplication.presentation.Screen
import com.alperenturker.myapplication.presentation.favorites.views.FavoritesSectionScreen
import com.alperenturker.myapplication.presentation.movie_detail.views.MovieDetailScreen
import com.alperenturker.myapplication.presentation.movies.views.MovieScreen
import com.alperenturker.myapplication.util.Constants.IMDB_ID
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val hostVm: AnalyticsHostViewModel = hiltViewModel()

    // Route değiştikçe otomatik ekran logla
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collectLatest { entry ->
            val raw = entry.destination.route.orElse("unknown")
            val screenName = raw
                .substringBefore("?")
                .replace(Regex("\\{.*?\\}"), "") // "detail/{id}" -> "detail/"
                .trimEnd('/')                    // "detail/"
            hostVm.logger.logScreen(screenName)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.MovieScreen.route
    ) {
        composable(route = Screen.MovieScreen.route) {
            MovieScreen(navController = navController)
        }
        composable(route = Screen.MovieDetailScreen.route + "/{${IMDB_ID}}") {
            MovieDetailScreen()
        }
        composable(route = Screen.FavoritesScreen.route) {
            FavoritesSectionScreen(
                onItemClick = { movie ->
                    navController.navigate(
                        Screen.MovieDetailScreen.route + "/${movie.imdbID}"
                    )
                }
            )
        }
    }
}