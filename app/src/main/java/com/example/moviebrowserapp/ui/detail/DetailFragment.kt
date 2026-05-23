package com.example.moviebrowser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviebrowser.MovieApp
import com.example.moviebrowser.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory((requireActivity().application as MovieApp).repository)
    }

    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as MovieApp
        currentUserId = app.sessionManager.getUserId()

        viewModel.loadMovie(args.movieId, currentUserId)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailUiState.Loading -> {
                    binding.tvTitle.text = "Зареждане..."
                    binding.tvOverview.text = ""
                    binding.tvReleaseDate.text = ""
                    binding.fabFavorite.visibility = View.GONE
                }

                is DetailUiState.Success -> {
                    val movie = state.movie

                    binding.fabFavorite.visibility = View.VISIBLE
                    binding.tvTitle.text = movie.title
                    binding.tvOverview.text = movie.overview
                    binding.tvReleaseDate.text = "📅 ${movie.releaseDate ?: "Няма дата"}"
                    binding.chipRating.text = "⭐ ${"%.1f".format(movie.voteAverage)}"

                    Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                        .placeholder(com.example.moviebrowser.R.drawable.ic_movie_placeholder)
                        .into(binding.ivPoster)

                    Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w780${movie.posterPath}")
                        .placeholder(com.example.moviebrowser.R.drawable.ic_movie_placeholder)
                        .into(binding.ivBackdrop)

                    binding.fabFavorite.setOnClickListener {
                        viewModel.toggleFavorite(movie, currentUserId)
                    }
                }

                is DetailUiState.Error -> {
                    binding.tvTitle.text = "Грешка"
                    binding.tvOverview.text = state.message
                    binding.fabFavorite.visibility = View.GONE
                }
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFav ->
            binding.fabFavorite.setImageResource(
                if (isFav) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}