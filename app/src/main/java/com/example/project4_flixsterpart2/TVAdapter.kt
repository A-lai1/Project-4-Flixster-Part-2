package com.example.project4_flixsterpart2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.campgrounds.R

private const val TAG = "TVShowAdapter"

class TVShowAdapter(
    private val context: Context,
    private val tvShows: List<TVShow>
) : RecyclerView.Adapter<TVShowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tvShow = tvShows[position]
        holder.bind(tvShow)
    }

    override fun getItemCount() = tvShows.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val nameTextView = itemView.findViewById<TextView>(R.id.tvName)
        private val ratingTextView = itemView.findViewById<TextView>(R.id.tvRating)
        private val imageView = itemView.findViewById<ImageView>(R.id.tvImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(tvShow: TVShow) {
            nameTextView.text = tvShow.name
            ratingTextView.text = "Rating: ${tvShow.voteAverage?.let { "★ $it" } ?: "N/A"}"

            Glide.with(context)
                .load(tvShow.fullPosterPath)
                .into(imageView)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position == RecyclerView.NO_POSITION) return
            val tvShow = tvShows[position]

            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(TV_SHOW_EXTRA, tvShow)
            }
            context.startActivity(intent)
        }
    }
}
