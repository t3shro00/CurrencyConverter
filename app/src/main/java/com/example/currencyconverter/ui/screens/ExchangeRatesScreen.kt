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
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.components.CurrencyDropdown
import com.example.currencyconverter.viewmodel.PairConversionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRatesScreen(
    viewModel: PairConversionViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var selectedCurrency by remember { mutableStateOf("USD") }
    var currencyCode by remember { mutableStateOf("") }
    var exchangeRatesMap by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(false) }

    val apiKey = stringResource(R.string.apiKey)

    // Fetch data when currency code or selected currency changes
    LaunchedEffect(currencyCode, selectedCurrency) {
        if (currencyCode.isNotBlank() || selectedCurrency.isNotBlank()) {
            isLoading = true
            val currencyToFetch = if (currencyCode.isNotBlank()) currencyCode else selectedCurrency
            viewModel.fetchExchangeRates(apiKey, currencyToFetch)
        }
    }

    val exchangeRatesResponse by viewModel.exchangeRates.observeAsState(initial = null)
    val message by viewModel.message.observeAsState(initial = "")
    val errorMessage by viewModel.errorMessage.observeAsState(initial = "")

    LaunchedEffect(exchangeRatesResponse) {
        isLoading = false
        exchangeRatesResponse?.let {
            exchangeRatesMap = it.rates
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier
                .height(24.dp)
        )
        // Title
//        Text(
//            text = "Exchange Rates",
//            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp),
//            color = MaterialTheme.colorScheme.primary,
//            fontWeight = FontWeight.Bold
//        )

        Spacer(modifier = Modifier.height(24.dp))

        // Currency Code Input
        TextField(
            value = currencyCode,
            onValueChange = { currencyCode = it },
            label = { Text("Enter Currency Code") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),

            )

        Spacer(modifier = Modifier.height(16.dp))

        // Currency Dropdown
//        CurrencyDropdown(
//            label = "Select Currency",
//            selectedCurrency = selectedCurrency,
//            onCurrencySelected = { selectedCurrency = it },
//            currencyList = viewModel.currencyList.value ?: emptyMap(),
//            modifier = Modifier.fillMaxWidth()
//        )

        Spacer(modifier = Modifier.height(20.dp))

        // Loading Indicator or Exchange Rates
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(50.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(exchangeRatesMap.toList()) { (currency, rate) ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = currency, style = MaterialTheme.typography.bodyLarge)
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

        Spacer(modifier = Modifier.height(20.dp))

        // Error Message
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Status Message
        if (message?.isNotBlank() == true) {
            Text(
                text = message ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }


    }
}