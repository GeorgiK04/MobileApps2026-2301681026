package com.example.moviebrowser.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebrowser.R
import com.example.moviebrowser.data.local.MovieEntity
import com.example.moviebrowser.databinding.ItemMovieBinding

class FavoritesAdapter(
    private val onMovieClick: (MovieEntity) -> Unit,
    private val onRemoveClick: (MovieEntity) -> Unit
) : ListAdapter<MovieEntity, FavoritesAdapter.FavoriteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.tvTitle.text = movie.title
            binding.tvOverview.text = movie.overview
            binding.tvReleaseDate.text = movie.releaseDate ?: "Няма дата"
            binding.chipRating.text = "⭐ ${"%.1f".format(movie.voteAverage)}"

            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w200${movie.posterPath}")
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(binding.ivPoster)

            binding.root.setOnClickListener { onMovieClick(movie) }
            binding.chipRating.setOnLongClickListener {
                onRemoveClick(movie)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
            oldItem == newItem
    }
}