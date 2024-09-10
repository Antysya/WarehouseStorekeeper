package com.example.warehouse.storekeeper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.warehouse.storekeeper.R
import com.example.warehouse.storekeeper.repositories.MainRepository
import com.example.warehouse.storekeeper.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    mainRepository: MainRepository,
    onLogin: () -> Unit,
    viewModel: LoginViewModel = viewModel { LoginViewModel(mainRepository) }
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            //verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6650a4))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Вход в приложение",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            val error = viewModel.error.intValue
            Text(if (error == 0) "" else stringResource(error))

            OutlinedTextField(
                value = viewModel.login.value,
                onValueChange = { viewModel.login.value = it },
                label = { Text(stringResource(R.string.login_login)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = { viewModel.password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(stringResource(R.string.login_password)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions {
                    viewModel.login(onLogin)
                }
            )
            if (viewModel.loading.value) {
                CircularProgressIndicator(Modifier.height(48.dp))
            } else {
                Button(onClick = { viewModel.login(onLogin) }) {
                    Text(stringResource(R.string.login_log_in))
                }
            }
        }
    }
}