package com.example.currencyconverter.ui.screens

import ConversionResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.components.CurrencyDropdown
import com.example.currencyconverter.ui.components.CurrencyInputField
import com.example.currencyconverter.ui.theme.CustomBackground
import com.example.currencyconverter.ui.theme.CustomPrimary
import com.example.currencyconverter.ui.theme.CustomSecondary
import com.example.currencyconverter.ui.viewmodel.CurrencyConverterViewModel
import com.example.currencyconverter.ui.viewmodel.UiState

@Composable
fun PairConversionScreen(
    viewModel: CurrencyConverterViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var baseCurrency by remember { mutableStateOf("EUR") }
    var targetCurrency by remember { mutableStateOf("GBP") }
    var amountText by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf("0.0") }

    val apiKey = stringResource(R.string.apiKey)
    val conversionResponse by viewModel.conversionRate.collectAsState()
    val currencyList by viewModel.currencyList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(true) {
        viewModel.fetchCurrencyList(apiKey)
    }

    LaunchedEffect(conversionResponse) {
        conversionResponse?.conversionResult?.let { result ->
            convertedAmount = result.toString()
            keyboardController?.hide()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        when (val currentState = uiState) {
            is UiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.fetching_conversion_rates), style = MaterialTheme.typography.bodyMedium)
                }
            }
            is UiState.Success -> {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CustomBackground,
                        contentColor = CustomPrimary,
                        ), // Custom background color
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Base currency dropdown
                        CurrencyDropdown(
                            label = stringResource(R.string.base_currency),
                            selectedCurrency = baseCurrency,
                            onCurrencySelected = { baseCurrency = it },
                            currencyList = currencyList ?: emptyMap(),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Target currency dropdown
                        CurrencyDropdown(
                            label = stringResource(R.string.target_currency),
                            selectedCurrency = targetCurrency,
                            onCurrencySelected = { targetCurrency = it },
                            currencyList = currencyList ?: emptyMap(),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Amount input field
                        CurrencyInputField(
                            label = "Amount",
                            value = amountText,
                            isNumeric = true,
                            onValueChange = { amountText = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        if (amount > 0) {
                            viewModel.fetchConversionRateWithAmount(apiKey, baseCurrency, targetCurrency, amount)
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = currentState !is UiState.Loading
                ) {
                    Text(stringResource(R.string.convertButton))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentState.message ?: stringResource(R.string.conversion_successful),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ConversionResult(
                    isLoading = false,
                    convertedAmount = convertedAmount,
                    baseCurrency = baseCurrency,
                    targetCurrency = targetCurrency
                )
            }
            is UiState.Error -> {
                Text(
                    text = currentState.message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.exchangeRateFluctuationInfo),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}
