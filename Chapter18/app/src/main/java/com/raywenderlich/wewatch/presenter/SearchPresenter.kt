package com.raywenderlich.wewatch.presenter

import com.raywenderlich.wewatch.data.MovieInteractor
import com.raywenderlich.wewatch.domain.MovieState
import com.raywenderlich.wewatch.view.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchPresenter(private val movieInteractor: MovieInteractor) {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: SearchView

    fun bind(view: SearchView) {
        this.view = view
        compositeDisposable.add(observeMovieDisplayIntent())
        compositeDisposable.add(observeAddMovieIntent())
        compositeDisposable.add(observeConfirmIntent())
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun observeConfirmIntent() = view.confirmIntent()
        .observeOn(Schedulers.io())
        .flatMap<MovieState> { movieInteractor.addMovie(it) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { view.render(it) }

    private fun observeAddMovieIntent() = view.addMovieIntent()
        .map<MovieState> { MovieState.ConfirmationState(it) }
        .subscribe { view.render(it) }

    private fun observeMovieDisplayIntent() = view.displayMoviesIntent()
        .flatMap<MovieState> { movieInteractor.searchMovies(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { view.render(MovieState.LoadingState) }
        .subscribe { view.render(it) }
}
