package com.raywenderlich.wewatch.presenter

import androidx.lifecycle.Observer
import com.raywenderlich.wewatch.MainContract
import com.raywenderlich.wewatch.data.entity.Movie
import com.raywenderlich.wewatch.view.activities.AddMovieActivity
import com.raywenderlich.wewatch.view.activities.MainActivity
import ru.terrakok.cicerone.Router

class MainPresenter(private var view: MainContract.View?,
                    private var interactor: MainContract.Interactor?,
                    private val router: Router?) : MainContract.Presenter, MainContract.InteractorOutput {

    override fun addMovie() {
        router?.navigateTo(AddMovieActivity.TAG)
    }

    override fun deleteMovies(selectedMovies: HashSet<*>) {
        for (movie in selectedMovies) {
            interactor?.delete(movie as Movie)
        }
    }

    override fun onViewCreated() {
        view?.showLoading()
        interactor?.loadMovieList()?.observe((view as MainActivity), Observer { movieList ->
            if (movieList != null) {
                onQuerySuccess(movieList)
            } else {
                onQueryError()
            }
        })
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onQuerySuccess(data: List<Movie>) {
        view?.hideLoading()
        view?.displayMovieList(data)
    }

    override fun onQueryError() {
        view?.hideLoading()
        view?.showMessage("Error Loading Data")
    }
}
