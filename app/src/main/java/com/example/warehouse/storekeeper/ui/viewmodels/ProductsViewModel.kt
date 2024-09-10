package com.example.warehouse.storekeeper.ui.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warehouse.storekeeper.R
import com.example.warehouse.storekeeper.data.Product
import com.example.warehouse.storekeeper.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsViewModel(
    private val mainRepository: MainRepository,
    private val orderId: Int
) : ViewModel() {
    val error = mutableIntStateOf(0)
    val products = mutableStateListOf<Product>()
    val loading = mutableStateOf(false)
    val canConfirm = mutableStateOf(false)
    val confirmLoading = mutableStateOf(false)

    init {
        loadProducts()
    }

    private fun loadProducts() {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getProducts(orderId).collect { result ->
                withContext(Dispatchers.Main) {
                    if (result == null) {
                        error.intValue = R.string.products_error
                    } else {
                        for (item in result) {
                            item.orderId = orderId
                        }
                        products.clear()
                        products.addAll(result)
                        updateCanConfirm()
                    }
                    loading.value = false
                }
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateProduct(product)
        }

        updateCanConfirm()
    }

    private fun updateCanConfirm() {
        var newCanConfirm = true
        val products = products.toList()
        for (product in products) {
            if (product.checked) continue

            newCanConfirm = false
            break
        }

        canConfirm.value = newCanConfirm
    }

    fun confirmOrder(onConfirmed: () -> Unit) {
        confirmLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.confirmOrder(orderId).collect { result ->
                withContext(Dispatchers.Main) {
                    if (result) {
                        onConfirmed()
                    } else {
                        error.intValue = R.string.products_confirm_error
                    }
                    confirmLoading.value = false
                }
            }
        }
    }
}