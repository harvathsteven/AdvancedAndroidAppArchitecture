package com.raywenderlich.wewatch.interactor

import com.raywenderlich.wewatch.AddContract
import com.raywenderlich.wewatch.data.MovieRepositoryImpl
import com.raywenderlich.wewatch.data.entity.Movie

class AddInteractor : AddContract.Interactor {

    private val repository: MovieRepositoryImpl = MovieRepositoryImpl()

    override fun addMovie(movie: Movie) = repository.saveMovie(movie)

}
