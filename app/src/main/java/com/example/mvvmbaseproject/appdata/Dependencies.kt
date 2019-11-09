package com.example.mvvmbaseproject.appdata

import android.content.Context
import com.example.mvvmbaseproject.appdata.Keys.Prefs.Companion.LIST_OF_SEARCHED_MOVIE
import io.paperdb.Paper

class Dependencies : Keys.Prefs {
    companion object {
        private var listOfSerachedMovie: ArrayList<String>? = null
        private val TAG = Dependencies::class.java.name

        fun getListOfSearchedMovie(): ArrayList<String>? {
            if(listOfSerachedMovie==null)
                listOfSerachedMovie=Paper.book()
                    .read(LIST_OF_SEARCHED_MOVIE, ArrayList())
            return listOfSerachedMovie
        }

        fun setListOfSearchedMovie(
            context:Context,
            listOfSerachedMovies: ArrayList<String>?
        ){
            Paper.init(context)
            this.listOfSerachedMovie = listOfSerachedMovies
            if (listOfSerachedMovie == null) {
                Paper.book().delete(LIST_OF_SEARCHED_MOVIE)
            } else {
                Paper.book().write(LIST_OF_SEARCHED_MOVIE, listOfSerachedMovie)
            }
        }


    }


}