package com.codemobiles.myandroid.network

import com.codemobiles.myandroid.models.ProductList
import com.codemobiles.myandroid.utilities.API_PRODUCT
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface NetworkAPI {
    @GET(API_PRODUCT)
    fun getProducts(): Call<ProductList>

    // การส่งแบบ form data
    @Multipart
    @POST(API_PRODUCT)
    fun addProduct(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>, // ถ้าส่งแค่รูปลบบรรทัดนี้ทิ้ง
        @Part image: MultipartBody.Part?
    ): Call<ResponseBody>

}



// https://cmpos-demo.herokuapp.com/products (GET)


