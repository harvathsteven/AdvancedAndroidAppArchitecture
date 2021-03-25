package com.raywenderlich.wewatch.presenter

import com.raywenderlich.wewatch.data.MovieInteractor
import com.raywenderlich.wewatch.domain.MovieState
import com.raywenderlich.wewatch.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter(private val movieInteractor: MovieInteractor) {
    private lateinit var view: MainView
    private val compositeDisposable = CompositeDisposable()

    fun bind(view: MainView) {
        this.view = view
        compositeDisposable.add(observeMovieDeleteIntent())
        compositeDisposable.add(observeMovieDisplay())
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun observeMovieDeleteIntent() = view.deleteMovieIntent()
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.io())
        .flatMap<Unit> { movieInteractor.deleteMovie(it) }
        .subscribe()

    private fun observeMovieDisplay() = movieInteractor.getMovieList()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { view.render(MovieState.LoadingState) }
        .doOnNext { view.render(it) }
        .subscribe()
}