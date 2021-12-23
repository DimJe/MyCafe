package com.Dimje.mymap

data class Cafeinfo(
    val documents: List<Document>,
    val meta : Meta

)
data class Document(
        val address_name: String,
        val category_group_code: String,
        val category_group_name: String,
        val category_name: String,
        val distance: String,
        val id: String,
        val phone: String,
        val place_name: String,
        val place_url: String,
        val road_address_name: String,
        val x: String,
        val y: String

)
data class Meta(
        val same_name: Same_name,
        val is_end : Boolean
)
data class Same_name(
        val keyword : String
)