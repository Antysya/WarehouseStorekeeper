package com.example.warehouse.storekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.warehouse.storekeeper.repositories.MainRepository
import com.example.warehouse.storekeeper.ui.AppNavigation
import com.example.warehouse.storekeeper.ui.theme.WarehouseStorekeeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainRepository = remember {
                MainRepository(applicationContext)
            }
            WarehouseStorekeeperTheme {
                AppNavigation(mainRepository)
            }
        }
    }
}