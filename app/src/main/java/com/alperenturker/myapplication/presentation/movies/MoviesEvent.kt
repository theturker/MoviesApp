package com.alperenturker.myapplication.presentation.movies

sealed class MoviesEvent{
    data class Search(val searchString : String) : MoviesEvent()
}