package com.example.mvvmbaseproject.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmbaseproject.R
import com.example.mvvmbaseproject.Utility.Utils
import com.example.mvvmbaseproject.appdata.Dependencies
import com.example.mvvmbaseproject.model.MovieData
import com.example.mvvmbaseproject.model.MovieDataPro
import com.example.mvvmbaseproject.ui.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.main_fragment.*

open class MainFragment : Fragment() {
    private lateinit var mGridLayoutManager: GridLayoutManager
    private var searchKey: String = ""

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        fetchDataFromServer()
        main.setOnRefreshListener {
            fetchDataFromServer()
        }

    }
    /**
     * ************************* Function used to update searched movie key ************************
     * */

    fun updateSearchKey(searchKey: String) {
        this.searchKey = searchKey
        var listOfSearched = Dependencies.getListOfSearchedMovie()
        if (listOfSearched == null || listOfSearched.size <= 0) {
            listOfSearched = ArrayList()
            listOfSearched.add(searchKey)
            Dependencies.setListOfSearchedMovie(context!!, listOfSearched)
        } else {
            listOfSearched.add(searchKey)
            Dependencies.setListOfSearchedMovie(context!!, listOfSearched)
        }
        fetchDataFromServer()
    }
    /**
     * *******************************Function used to make api hit*********************************
     * */

    private fun fetchDataFromServer() {
        viewModel.fetchData(context!!, searchKey)
        viewModel.data.observe(this,
            Observer<MovieDataPro> {
                initializeAdapter(it)
            })

        viewModel.loading.observe(this, Observer { isLoading ->
            run {
                if (!isLoading) {
                    main.isRefreshing = false
                }
            }
        })


    }


    /**
     * *****Function used to initialise adapter and setting list to movies_recycler_view************
     * */

    private fun initializeAdapter(it: MovieDataPro) {
        val list: java.util.ArrayList<MovieData>? = it.search
        val mMoviesAdapter = MoviesAdapter(this, list)
        if (!list.isNullOrEmpty() && list.size > 0) {
            Utils.setVisibility(View.VISIBLE, movies_recycler_view)
            Utils.setVisibility(View.GONE, tvNoMovieFound, ivEggCry)
        } else {
            Utils.setVisibility(View.GONE, movies_recycler_view)
            Utils.setVisibility(View.VISIBLE, tvNoMovieFound, ivEggCry)
        }
        mGridLayoutManager =
            GridLayoutManager(activity, resources.getInteger(R.integer.movies_columns))
        mGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return  1
            }
        }

        movies_recycler_view.layoutManager = mGridLayoutManager
        movies_recycler_view.adapter = mMoviesAdapter

    }


}
