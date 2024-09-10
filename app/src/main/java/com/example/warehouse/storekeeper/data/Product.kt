package com.example.warehouse.storekeeper.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val quantity: Int,
    val status: String,
    var orderId: Int,
    var checked: Boolean = when (status) {
        "Обработан" -> true
        else -> false
    }
)

