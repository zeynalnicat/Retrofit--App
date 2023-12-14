package com.matrix.android_104_android.retrofit

import com.matrix.android_104_android.model.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi
{

    @GET("products")
    suspend fun getProducts(): Response<Products>

    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>

    @GET( "products/category/{category}")
    suspend fun getSpecific(@Path("category") category:String):Response<Products>

}