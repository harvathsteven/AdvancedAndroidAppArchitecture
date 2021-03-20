package com.raywenderlich.ana.movietheatreexample

import dagger.Component

@Component
interface MovieTheatreComponent {
    fun getMovieTheatre() : MovieTheatre
}