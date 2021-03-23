package com.raywenderlich.wewatch.interactor

import androidx.lifecycle.MediatorLiveData
import com.raywenderlich.wewatch.MainContract
import com.raywenderlich.wewatch.data.MovieRepositoryImpl
import com.raywenderlich.wewatch.data.entity.Movie

class MainInteractor : MainContract.Interactor {
    private val movieList = MediatorLiveData<List<Movie>>()
    private val repository: MovieRepositoryImpl = MovieRepositoryImpl()

    init {
        getAllMovies()
    }

    override fun loadMovieList() = movieList

    override fun delete(movie: Movie) = repository.deleteMovie(movie)

    override fun getAllMovies() {
        movieList.addSource(repository.getSavedMovies()) { movies ->
            movieList.postValue(movies)
        }
    }
}
