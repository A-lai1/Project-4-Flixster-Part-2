package com.example.project4_flixsterpart2

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TVShowResponse(
    @SerialName("results")
    val results: List<TVShow>?
) : java.io.Serializable

@Keep
@Serializable
data class TVShow(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("name")
    val name: String?,

    @SerialName("overview")
    val overview: String?,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("number_of_seasons")
    val numberOfSeasons: Int? = null,

    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @SerialName("vote_average")
    val voteAverage: Double? = null
) : java.io.Serializable {

    // Helper property to get the full image URL formatted for TMDB
    val fullPosterPath: String
        get() = if (!posterPath.isNullOrEmpty()) {
            "https://image.tmdb.org/t/p/w500$posterPath"
        } else {
            ""
        }
}
