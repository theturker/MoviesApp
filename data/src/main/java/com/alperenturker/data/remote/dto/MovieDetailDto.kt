package com.alperenturker.data.remote.dto

import com.alperenturker.domain.model.MovieDetail
import com.alperenturker.domain.model.Rating as DomainRating

// OMDb bazı alanları göndermediği için hepsini nullable yapıyoruz
data class MovieDetailDto(
    val Actors: String?,
    val Awards: String?,
    val BoxOffice: String?,
    val Country: String?,
    val DVD: String?,
    val Director: String?,
    val Genre: String?,
    val Language: String?,
    val Metascore: String?,
    val Plot: String?,
    val Poster: String?,
    val Production: String?,
    val Rated: String?,
    val Ratings: List<Rating>?,   // DTO Rating sende zaten var
    val Released: String?,
    val Response: String?,
    val Runtime: String?,
    val Title: String?,
    val Type: String?,
    val Website: String?,
    val Writer: String?,
    val Year: String?,
    val imdbID: String?,
    val imdbRating: String?,
    val imdbVotes: String?,
    // OMDb “False” döndüğünde genelde Error da gelir:
    val Error: String? = null
)

// "N/A" veya boş/null ise default dön
private fun String?.clean(default: String = "-"): String {
    val s = this?.trim()
    return if (s.isNullOrEmpty() || s.equals("N/A", ignoreCase = true)) default else s
}

// Görsel/link gibi alanlarda boş string daha doğru (UI placeholder çalışsın)
private fun String?.cleanEmpty(): String {
    val s = this?.trim()
    return if (s.isNullOrEmpty() || s.equals("N/A", ignoreCase = true)) "" else s
}

fun MovieDetailDto.toMovieDetail(): MovieDetail {
    // API “Response”: "False" ise hata fırlatalım (UI try/catch ile gösterir)
    if (Response?.equals("False", ignoreCase = true) == true) {
        throw IllegalStateException(Error ?: "Movie not found")
    }

    return MovieDetail(
        Actors   = Actors.clean(),
        Awards   = Awards.clean(),
        BoxOffice= BoxOffice.clean(),        // 🔴 Crash sebebi: artık default var
        Country  = Country.clean(),
        DVD      = DVD.clean(),
        Director = Director.clean(),
        Genre    = Genre.clean(),
        Language = Language.clean(),
        Metascore= Metascore.clean(),
        Plot     = Plot.clean(),
        Poster   = Poster.cleanEmpty(),      // boşsa Coil placeholder çalışsın
        Production = Production.clean(),
        Rated    = Rated.clean(),
        Ratings  = (Ratings ?: emptyList()).map {
            DomainRating(
                Source = it.Source?.clean() ?: "-",
                Value = it.Value?.clean() ?: "-"
            )
        },
        Released = Released.clean(),
        Response = (Response ?: "True"),     // domain non-null ise bir şey ver
        Runtime  = Runtime.clean(),
        Title    = Title.clean("-"),
        Type     = Type.clean(),
        Website  = Website.cleanEmpty(),
        Writer   = Writer.clean(),
        Year     = Year.clean(),
        imdbID   = imdbID.clean(),
        imdbRating = imdbRating.clean(),
        imdbVotes  = imdbVotes.clean()
    )
}
