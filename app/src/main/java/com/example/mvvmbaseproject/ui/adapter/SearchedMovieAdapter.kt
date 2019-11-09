package com.example.mvvmbaseproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmbaseproject.R
import kotlinx.android.synthetic.main.item_searched.view.*

class SearchedMovieAdapter(private var list: ArrayList<String>) :RecyclerView.Adapter<SearchedMovieAdapter.SearchedMovieHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedMovieHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_searched, parent, false)
        return SearchedMovieHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchedMovieHolder, position: Int) {
        holder.itemView.tvSearchedMovieName.text=list[position]
    }


    inner class SearchedMovieHolder(view: View) : RecyclerView.ViewHolder(view)

}