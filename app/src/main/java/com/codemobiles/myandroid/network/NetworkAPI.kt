package com.codemobiles.myandroid.network

import com.codemobiles.myandroid.models.ProductList
import com.codemobiles.myandroid.utilities.API_PRODUCT
import retrofit2.Call
import retrofit2.http.GET


interface NetworkAPI {
    @GET(API_PRODUCT)
    fun getProducts(): Call<ProductList>

}

// https://cmpos-demo.herokuapp.com/products (GET)


