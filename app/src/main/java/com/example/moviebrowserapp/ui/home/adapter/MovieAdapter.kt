package com.example.moviebrowser.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebrowser.R
import com.example.moviebrowser.data.remote.MovieDto
import com.example.moviebrowser.databinding.ItemMovieBinding

class MovieAdapter(
    private val onMovieClick: (MovieDto) -> Unit
) : ListAdapter<MovieDto, MovieAdapter.MovieViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDto) {
            binding.tvTitle.text = movie.title
            binding.tvOverview.text = movie.overview
            binding.tvReleaseDate.text = movie.releaseDate ?: "Няма дата"
            binding.chipRating.text = "⭐ ${"%.1f".format(movie.voteAverage)}"

            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w200${movie.posterPath}")
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(binding.ivPoster)

            binding.root.setOnClickListener { onMovieClick(movie) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieDto>() {
        override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto) =
            oldItem == newItem
    }
}