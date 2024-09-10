package com.example.warehouse.storekeeper.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.warehouse.storekeeper.data.Order
import com.example.warehouse.storekeeper.data.Product

@Database(entities = [Order::class, Product::class], version = 7)
abstract class AppDatabase: RoomDatabase() {
    abstract val orderDao: OrderDao
    abstract val productDao: ProductDao

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "main.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}