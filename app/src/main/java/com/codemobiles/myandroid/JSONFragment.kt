package com.codemobiles.myandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.myandroid.databinding.ActivityMainBinding
import com.codemobiles.myandroid.databinding.CustomListBinding
import com.codemobiles.myandroid.databinding.FragmentJsonBinding
import com.codemobiles.myandroid.models.ProductList
import com.codemobiles.myandroid.network.NetworkAPI
import com.codemobiles.myandroid.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JSONFragment : Fragment() {

    private lateinit var binding: FragmentJsonBinding
    private lateinit var adapter: CustomAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        feedNetwork()

        binding = FragmentJsonBinding.inflate(inflater, container, false)

        adapter = CustomAdapter(arrayListOf())
        binding.recyclerview.adapter = adapter
        // important
        binding.recyclerview.layoutManager = GridLayoutManager(context, 2)
//        binding.recyclerview.layoutManager = LinearLayoutManager(context)
//        binding.recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        return binding.root
    }

    private fun feedNetwork() {
        val call = NetworkService.getClient().create(NetworkAPI::class.java).getProducts()
        call.enqueue(object : Callback<ProductList>{
            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {
                if(response.isSuccessful) {
                    adapter.products = response.body()!!
                    //important
                    adapter.notifyDataSetChanged()
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

    inner class CustomAdapter(var products: ProductList): RecyclerView.Adapter<CustomAdapter.MyViewHolder>(){

        inner class MyViewHolder(val customListBinding: CustomListBinding): RecyclerView.ViewHolder(customListBinding.root) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val binding = CustomListBinding.inflate(layoutInflater)
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val product = products[position]
            val binding = holder.customListBinding
            binding.textviewName.text = product.name
            binding.textviewDetail.text = product.createdAt
            binding.textviewPrice.text = product.price.toString()
            binding.textviewStock.text = product.stock.toString()

            Glide
                .with(holder.itemView.context)
                .load(NetworkService.getImageURL() + product.image)
//                .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                .into(binding.imageviewProduct)

        }

        override fun getItemCount() = products.size

    }
}