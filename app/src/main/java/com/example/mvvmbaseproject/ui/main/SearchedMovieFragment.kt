package com.example.mvvmbaseproject.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mvvmbaseproject.R
import com.example.mvvmbaseproject.Utility.Utils
import com.example.mvvmbaseproject.appdata.Dependencies
import com.example.mvvmbaseproject.ui.adapter.SearchedMovieAdapter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SearchedMovieFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var view1: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_searched_movie, container, false)
        setAdapter()

        return view1

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    private fun setAdapter() {
        val listOfSaerchedMovie = Dependencies.getListOfSearchedMovie()
        val mMoviesAdapter = SearchedMovieAdapter(listOfSaerchedMovie!!)
        val rvSearchedMovie = view1?.findViewById<RecyclerView>(R.id.rvSearchedMovie)
        val tvNoSearchResult=view1?.findViewById<TextView>(R.id.tvNoSearchResult)
        val ivEggCrySearch=view1?.findViewById<ImageView>(R.id.ivEggCrySearch)

        if (!listOfSaerchedMovie.isNullOrEmpty() && listOfSaerchedMovie.size > 0) {
            Utils.setVisibility(View.VISIBLE, rvSearchedMovie!!)
            Utils.setVisibility(View.GONE, tvNoSearchResult!!, ivEggCrySearch!!)
        } else {
            Utils.setVisibility(View.GONE, rvSearchedMovie!!)
            Utils.setVisibility(View.VISIBLE, tvNoSearchResult!!, ivEggCrySearch!!)
        }
        val linearlayout =
            LinearLayoutManager(activity)
        rvSearchedMovie.layoutManager = linearlayout
        rvSearchedMovie.adapter = mMoviesAdapter
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchedMovieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
