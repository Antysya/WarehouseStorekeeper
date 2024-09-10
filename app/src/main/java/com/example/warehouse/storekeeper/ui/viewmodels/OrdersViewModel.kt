package com.example.warehouse.storekeeper.ui.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warehouse.storekeeper.R
import com.example.warehouse.storekeeper.data.Order
import com.example.warehouse.storekeeper.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val error = mutableIntStateOf(0)
    val orders = mutableStateListOf<Order>()
    val loading = mutableStateOf(false)

    init {
        loadOrders()
    }

    private fun loadOrders() {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getOrders().collect { result ->
                withContext(Dispatchers.Main) {
                    if (result == null) {
                        error.intValue = R.string.orders_error
                    } else {
                        orders.clear()
                        orders.addAll(result)
                    }
                    loading.value = false
                }
            }
        }
    }
}