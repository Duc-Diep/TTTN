package com.ducdiep.bookshop.models

data class Account(
    private var id: Int,
    val phone: String,
    val password: String,
    val fullName: String,
    val address: String
)
