package com.example.mvvmbaseproject.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class MovieDataPro:Serializable {

    @SerializedName("Search")
    @Expose
     val search: ArrayList<MovieData>? = null
}