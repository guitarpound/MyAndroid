package com.codemobiles.myandroid.network

import com.codemobiles.myandroid.utilities.BASE_URL
import com.codemobiles.myandroid.utilities.IMAGE_PATH
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// singleton
object NetworkService {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    fun getImageURL() = BASE_URL + "${IMAGE_PATH}/"
}