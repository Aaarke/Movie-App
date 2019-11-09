package com.example.mvvmbaseproject.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Rating : Serializable {
    @SerializedName("Source")
    @Expose
    val source: String? = null
    @SerializedName("Value")
    @Expose
    val value: String? = null
}