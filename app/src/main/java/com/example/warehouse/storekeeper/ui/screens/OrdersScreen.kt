package com.example.warehouse.storekeeper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.warehouse.storekeeper.repositories.MainRepository
import com.example.warehouse.storekeeper.ui.viewmodels.OrdersViewModel

@Composable
fun OrdersScreen(
    mainRepository: MainRepository,
    onOrderSelected: (id: Int) -> Unit,
) {
    val viewModel: OrdersViewModel = viewModel { OrdersViewModel(mainRepository) }
    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6650a4))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Заказы",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.loading.value) {
                    item {
                        CircularProgressIndicator()
                    }
                }

                val error = viewModel.error.intValue
                if (error != 0) {
                    item {
                        Text(stringResource(error))
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Название",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Тип",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider()
                }

                items(viewModel.orders) { order ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOrderSelected(order.id) }
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = order.name,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = order.typeName,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}