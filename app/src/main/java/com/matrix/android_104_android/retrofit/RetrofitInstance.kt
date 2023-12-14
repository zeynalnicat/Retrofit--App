package com.matrix.android_104_android.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    val productApi: ProductApi? = Retrofit.Builder().baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create()).build().create(ProductApi::class.java)
}