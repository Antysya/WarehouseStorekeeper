package com.example.warehouse.storekeeper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.warehouse.storekeeper.R
import com.example.warehouse.storekeeper.repositories.MainRepository
import com.example.warehouse.storekeeper.ui.viewmodels.ProductsViewModel

@Composable
fun ProductsScreen(
    mainRepository: MainRepository,
    orderId: Int,
    onConfirmed: () -> Unit,
) {
    val viewModel: ProductsViewModel = viewModel { ProductsViewModel(mainRepository, orderId) }

    Scaffold { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF6650a4))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Список товаров",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            if (viewModel.loading.value) {
                item {
                    CircularProgressIndicator()
                }
            }
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Название",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Количество",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Собран?",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                HorizontalDivider()
            }

            items(viewModel.products) { product ->
                Column {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.name.toString(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 16.dp)
                        )
                        Text(
                            text = product.quantity.toString(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 16.dp)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val checked = remember {
                                mutableStateOf(product.checked)
                            }
                            Switch(
                                checked = checked.value,
                                onCheckedChange = {
                                    checked.value = it
                                    product.checked = it
                                    viewModel.updateProduct(product)
                                }
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }

            val error = viewModel.error.intValue
            if (error != 0) {
                item {
                    Text(stringResource(error))
                }
            }

            if (viewModel.products.isNotEmpty()) {
                item {
                    if (viewModel.confirmLoading.value) {
                        CircularProgressIndicator(
                            Modifier
                                .padding(16.dp)
                                .height(48.dp)
                        )
                    } else {
                        Button(
                            onClick = {
                                viewModel.confirmOrder(onConfirmed)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            enabled = viewModel.canConfirm.value
                        ) {
                            Text(stringResource(R.string.products_confirm))
                        }
                    }
                }
            }
        }
    }
}