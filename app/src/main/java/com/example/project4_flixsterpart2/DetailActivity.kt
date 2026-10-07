package com.example.project4_flixsterpart2

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.campgrounds.BuildConfig
import com.codepath.campgrounds.R
import okhttp3.Headers

private const val TAG = "TVShowDetailActivity"
const val TV_SHOW_EXTRA = "TV_SHOW_EXTRA"

class DetailActivity : AppCompatActivity() {
    private lateinit var tvShowNameTV: TextView
    private lateinit var tvShowOverviewTV: TextView
    private lateinit var tvShowSeasonsTV: TextView
    private lateinit var tvShowEpisodesTV: TextView
    private lateinit var tvShowRatingTV: TextView
    private lateinit var tvShowPosterIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Find views in the layout
        tvShowNameTV = findViewById(R.id.tvName)
        tvShowOverviewTV = findViewById(R.id.tvOverview)
        tvShowSeasonsTV = findViewById(R.id.tvNumOfSeasons)
        tvShowEpisodesTV = findViewById(R.id.tvNumOfEpisodes)
        tvShowRatingTV = findViewById(R.id.tvRating)
        tvShowPosterIV = findViewById(R.id.tvPoster)

        // Retrieve the TVShow extra from Intent safely
        val tvShow = intent.getSerializableExtra(TV_SHOW_EXTRA) as? TVShow ?: run {
            finish()
            return
        }

        // Set initial summary text properties
        tvShowNameTV.text = tvShow.name
        tvShowOverviewTV.text = tvShow.overview
        tvShowSeasonsTV.text = "Seasons: ${tvShow.numberOfSeasons ?: "Loading..."}"
        tvShowEpisodesTV.text = "Episodes: ${tvShow.numberOfEpisodes ?: "Loading..."}"
        tvShowRatingTV.text = "Rating: ${tvShow.voteAverage?.let { "★ $it / 10" } ?: "N/A"}"

        // Load poster image using Glide
        Glide.with(this)
            .load(tvShow.fullPosterPath)
            .into(tvShowPosterIV)

        // Fetch detailed info (seasons & episodes) if ID is present
        tvShow.id?.let { showId ->
            val detailUrl = "https://api.themoviedb.org/3/tv/${showId}?api_key=${BuildConfig.API_KEY}"
            val client = AsyncHttpClient()
            client.get(detailUrl, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                    try {
                        val jsonObject = json.jsonObject
                        val seasons = jsonObject.optInt("number_of_seasons", -1)
                        val episodes = jsonObject.optInt("number_of_episodes", -1)

                        if (seasons != -1) {
                            tvShowSeasonsTV.text = "Seasons: $seasons"
                        }
                        if (episodes != -1) {
                            tvShowEpisodesTV.text = "Episodes: $episodes"
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing detail JSON: $e")
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.e(TAG, "Failed to fetch TV show details: $statusCode")
                }
            })
        }
    }
}
