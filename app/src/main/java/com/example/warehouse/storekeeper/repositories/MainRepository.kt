package com.example.warehouse.storekeeper.repositories

import android.content.Context
import android.util.Log
import com.example.warehouse.storekeeper.api.MainApi
import com.example.warehouse.storekeeper.data.LoginRequest
import com.example.warehouse.storekeeper.data.Product
import com.example.warehouse.storekeeper.db.AppDatabase
import kotlinx.coroutines.flow.flow

class MainRepository(context: Context) {

    private val mainApi = MainApi.getInstance()
    private val database = AppDatabase.getInstance(context)

    private var token = ""

    fun login(login: String, password: String) = flow {
        val request = LoginRequest(login, password)
        val responseBody = try {
            mainApi.login(request).body()
        } catch (_: Exception) {
            null
        }

        if (responseBody != null) {
            token = "bearer ${responseBody.token}"
        }
        emit(responseBody != null)
    }

    fun getOrders() = flow {
        val onlineOrders = try {
            mainApi.getOrders(token).body()
        } catch (_: Exception) {
            null
        }
        emit(onlineOrders)

        if (onlineOrders != null) {
            database.orderDao.upsertAll(onlineOrders)
        }
    }

    fun confirmOrder(orderId: Int) = flow {
        val result = try {
            mainApi.confirmOrder(orderId).isSuccessful
        } catch (_: Exception) {
            false
        }

        emit(result)
    }

    fun getProducts(orderId: Int) = flow {

        val onlineProducts = try {
            val response = mainApi.getProducts(token, orderId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("API Error", "Error code: ${response.code()}, message: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API Exception", e.toString())
            null
        }

        // Проверяем, получили ли мы продукты от API
        if (onlineProducts != null) {
            emit(onlineProducts) // Если получили, выводим их
            database.productDao.upsertAll(onlineProducts) // Сохраняем в локальную базу данных
        } else {
            Log.e("Product Fetch", "No products found for orderId: $orderId")
        }
    }

    suspend fun updateProduct(product: Product) {
        database.productDao.update(product)
    }

}

