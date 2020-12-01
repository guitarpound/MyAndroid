package com.codemobiles.myandroid.models

typealias ProductList = ArrayList<Product>

data class Product (
    val id: Long,
    val name: String,
    val image: String,
    val stock: Long,
    val price: Long,
    val createdAt: String,
    val updatedAt: String
)