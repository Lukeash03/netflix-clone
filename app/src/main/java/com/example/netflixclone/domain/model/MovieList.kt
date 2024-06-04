package com.example.netflixclone.domain.model

import com.google.gson.annotations.SerializedName

data class MovieList(
    val page: Int,
    @SerializedName("results")
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
