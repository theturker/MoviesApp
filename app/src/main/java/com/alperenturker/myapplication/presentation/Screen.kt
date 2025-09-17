package com.alperenturker.myapplication.presentation

sealed class Screen(val route : String){
    object MovieScreen : Screen("movie_screen")
    object MovieDetailScreen : Screen("movie_detail_screen")
    object FavoritesScreen : Screen("favorites_screen")
}
