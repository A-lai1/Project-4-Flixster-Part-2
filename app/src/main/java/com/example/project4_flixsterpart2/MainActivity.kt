package com.example.project4_flixsterpart2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.campgrounds.BuildConfig
import com.codepath.campgrounds.R
import com.codepath.campgrounds.databinding.ActivityMainBinding
//import com.example.project4_flixsterpart2.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json
import okhttp3.Headers

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "TVMain/"
private const val TV_API_KEY = BuildConfig.API_KEY
private const val TV_SHOWS_URL =
    "https://api.themoviedb.org/3/tv/top_rated?api_key=${TV_API_KEY}"

class MainActivity : AppCompatActivity() {
    private lateinit var tvShowsRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    private val tvShows = mutableListOf<TVShow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust ID according to your activity_main.xml (e.g., R.id.tv_shows or R.id.campgrounds)
        tvShowsRecyclerView = findViewById(R.id.tv_shows)

        val tvShowAdapter = TVShowAdapter(this, tvShows)
        tvShowsRecyclerView.adapter = tvShowAdapter

        tvShowsRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            tvShowsRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        val client = AsyncHttpClient()
        client.get(TV_SHOWS_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch TV shows: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched TV shows: $json")
                try {
                    val parsedJson = createJson().decodeFromString(
                        TVShowResponse.serializer(),
                        json.jsonObject.toString()
                    )

                    parsedJson.results?.let { list ->
                        tvShows.addAll(list)
                        tvShowAdapter.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Exception parsing JSON: $e")
                }
            }
        })
    }
}