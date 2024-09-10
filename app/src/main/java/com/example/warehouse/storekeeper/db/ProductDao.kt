package com.example.warehouse.storekeeper.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.warehouse.storekeeper.data.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM `Product` WHERE `orderId` = :orderId")
    suspend fun getAllForOrder(orderId: Int): List<Product>

    @Upsert
    suspend fun upsertAll(products: List<Product>)

    @Update
    suspend fun update(product: Product)
}