package com.example.mvvmbaseproject.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmbaseproject.R
import com.example.mvvmbaseproject.model.MovieData
import com.github.florent37.glidepalette.GlidePalette
import kotlinx.android.synthetic.main.item_movie.view.*
import java.util.*


class MoviesAdapter(private val mFragment: Fragment, movies: List<MovieData>?) :
    EndlessAdapter<MovieData, MoviesAdapter.MovieHolder>(
        mFragment.activity as Context, movies as MutableList<MovieData>?
            ?: ArrayList()
    ) {
    override fun run() {

    }

    private var mListener = OnMovieClickListener.DUMMY

    interface OnMovieClickListener {
        fun onContentClicked(movie: MovieData, view: View, position: Int)

        fun onFavoredClicked(movie: MovieData, position: Int)

        companion object {
            val DUMMY: OnMovieClickListener = object : OnMovieClickListener {
                override fun onContentClicked(movie: MovieData, view: View, position: Int) {}

                override fun onFavoredClicked(movie: MovieData, position: Int) {}
            }
        }
    }





    override fun onCreateItemHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(mInflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = mItems[position]
        holder.itemView.movie_item_container!!.setOnClickListener { view ->
            mListener.onContentClicked(
                movie,
                view,
                holder.adapterPosition
            )
        }
        holder.itemView.movie_item_image.post {
            try {
                Glide.with(mFragment.context)
                    .load(movie.poster)
                    .crossFade()
                    .placeholder(R.color.movie_poster_placeholder)
                    .listener(GlidePalette.with(movie.poster)
                        .intoCallBack { palette -> applyColors(palette.vibrantSwatch, holder) })
                    .into(holder.itemView.movie_item_image!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        holder.itemView.movie_item_image!!.setOnClickListener { view ->
            mListener.onContentClicked(
                movie,
                view,
                holder.adapterPosition
            )
        }

        holder.itemView.movie_item_title.text = movie.title
        holder.itemView.movie_item_genres.text = movie.genre


    }

    private fun applyColors(
        vibrantSwatch: Palette.Swatch?,
        holder: RecyclerView.ViewHolder
    ) {
        if (vibrantSwatch != null) {
            holder.itemView.movie_item_footer!!.setBackgroundColor(vibrantSwatch.rgb)
            holder.itemView.movie_item_title!!.setTextColor(vibrantSwatch.bodyTextColor)
            holder.itemView.movie_item_genres!!.setTextColor(vibrantSwatch.titleTextColor)
        }
    }

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view)
}

