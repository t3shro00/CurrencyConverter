package com.example.currencyconverter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.viewmodel.CurrencyConverterViewModel
import com.example.currencyconverter.ui.viewmodel.UiState

@Composable
fun ExchangeRatesScreen(
    viewModel: CurrencyConverterViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var currencyCode by remember { mutableStateOf("") }
    val apiKey = stringResource(R.string.apiKey)

    val uiState by viewModel.uiState.collectAsState()
    val exchangeRatesResponse by viewModel.exchangeRates.collectAsState()

    LaunchedEffect(currencyCode) {
        if (currencyCode.isNotBlank()) {
            viewModel.fetchExchangeRates(apiKey, currencyCode)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Currency Code Input
        TextField(
            value = currencyCode,
            onValueChange = { currencyCode = it },
            label = { Text(stringResource(R.string.enter_currency_code)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Add padding to the TextField
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(12.dp) // Rounded corners for the TextField
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (currencyCode.isBlank()) {
            Text(
                text = stringResource(R.string.please_enter_a_currency_code),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(50.dp)
                    )
                }
                is UiState.Success -> {
                    val exchangeRatesMap = exchangeRatesResponse?.rates.orEmpty()

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(exchangeRatesMap.toList()) { (currency, rate) ->
                            Card(
                                shape = RoundedCornerShape(12.dp), // Rounded corners for cards
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currency,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = String.format("%.2f", rate),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = stringResource(R.string.invalid_currency_code_please_try_again),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
