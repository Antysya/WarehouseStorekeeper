package com.example.warehouse.storekeeper.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.warehouse.storekeeper.data.Order

@Dao
interface OrderDao {
    @Query("SELECT * FROM `Order`")
    suspend fun getAll(): List<Order>

    @Upsert
    suspend fun upsertAll(products: List<Order>)
}