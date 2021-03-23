package com.raywenderlich.wewatch.interactor

import androidx.lifecycle.LiveData
import com.raywenderlich.wewatch.SearchContract
import com.raywenderlich.wewatch.data.MovieRepositoryImpl
import com.raywenderlich.wewatch.data.entity.Movie

class SearchInteractor : SearchContract.Interactor {
    private val repository: MovieRepositoryImpl = MovieRepositoryImpl()

    override fun searchMovies(title: String): LiveData<List<Movie>?> = repository.searchMovies(title)

    override fun addMovie(movie: Movie?) {
        movie?.let {
            repository.saveMovie(movie)
        }
    }
}
