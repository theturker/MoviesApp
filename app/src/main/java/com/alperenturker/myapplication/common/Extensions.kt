package com.alperenturker.myapplication.common

fun String?.orElse(fallback: String) =
    if (this.isNullOrBlank()) fallback else this