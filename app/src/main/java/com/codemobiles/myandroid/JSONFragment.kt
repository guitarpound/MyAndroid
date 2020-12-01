package com.codemobiles.myandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codemobiles.myandroid.models.ProductList
import com.codemobiles.myandroid.network.NetworkAPI
import com.codemobiles.myandroid.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JSONFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        feedNetwork()
        return inflater.inflate(R.layout.fragment_json, container, false)
    }

    private fun feedNetwork() {
        var call = NetworkService.getClient().create(NetworkAPI::class.java).getProducts()
        call.enqueue(object : Callback<ProductList>{
            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {
                if(response.isSuccessful) {
                    Log.d("my_network", response.body().toString())
                }else{
                    Log.e("my_network", "network fail")
                }
            }

            override fun onFailure(call: Call<ProductList>, t: Throwable) {
                Log.e("my_network", t.message.toString())
            }


        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = JSONFragment()
    }
}