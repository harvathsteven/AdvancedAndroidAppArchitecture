package com.raywenderlich.wewatch.data

import com.raywenderlich.wewatch.data.model.Movie
import com.raywenderlich.wewatch.data.net.RetrofitClient
import com.raywenderlich.wewatch.db
import com.raywenderlich.wewatch.domain.MovieState
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MovieInteractor : Interactor {
    private val retrofitClient = RetrofitClient()
    private val movieDao = db.movieDao()

    override fun getMovieList(): Observable<MovieState> {
        return movieDao.getAll()
            .map<MovieState> { MovieState.DataState(it) }
            .onErrorReturn {
                MovieState.ErrorState("Error")
            }
    }

    override fun deleteMovie(movie: Movie): Observable<Unit> = movieDao.delete(movie).toObservable()

    override fun searchMovies(title: String): Observable<MovieState> = retrofitClient.searchMovies(title)
        .observeOn(Schedulers.io())
        .map<MovieState> { it.results?.let { MovieState.DataState(it) } }
        .onErrorReturn { MovieState.ErrorState("Error") }

    override fun addMovie(movie: Movie): Observable<MovieState> = movieDao.insert(movie)
        .map<MovieState> {
            MovieState.FinishState
        }.toObservable()

}