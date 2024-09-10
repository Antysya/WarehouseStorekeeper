package com.example.warehouse.storekeeper.ui.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warehouse.storekeeper.R
import com.example.warehouse.storekeeper.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val error = mutableIntStateOf(0)
    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val loading = mutableStateOf(false)

    fun login(onLogin: () -> Unit) {
        loading.value = true
        error.intValue = 0
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.login(login.value, password.value).collect { result ->
                withContext(Dispatchers.Main) {
                    if (result) {
                        onLogin()
                    } else {
                        error.intValue = R.string.login_error
                    }
                    loading.value = false
                }
            }
        }
    }
}