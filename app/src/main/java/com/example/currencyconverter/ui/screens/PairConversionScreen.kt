package com.example.currencyconverter.ui.screens

import ConversionResult
import CurrencyInputField
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.ui.components.CurrencyDropdown
import com.example.currencyconverter.viewmodel.PairConversionViewModel

import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.example.currencyconverter.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairConversionScreen(
    viewModel: PairConversionViewModel,
    modifier: Modifier = Modifier
) {
    // Local state variables
    var baseCurrency by remember { mutableStateOf("EUR") }
    var targetCurrency by remember { mutableStateOf("GBP") }
    var amountText by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf("0.0") }
    var isLoading by remember { mutableStateOf(false) }
    var isInitialLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }  // For error message display

    val apiKey = stringResource(R.string.apiKey) // Store API key securely
    val conversionResponse by viewModel.conversionRate.observeAsState(initial = null)
    val message by viewModel.message.observeAsState(initial = null)
    val currencyList by viewModel.currencyList.observeAsState(initial = emptyMap())

    // Get the keyboard controller to hide the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    // Fetch currency list when the screen is first loaded
    LaunchedEffect(true) {
        viewModel.fetchCurrencyList(apiKey)
    }

    // Fetch conversion rate when amountText changes
    LaunchedEffect(amountText) {
        if (amountText.isNotBlank()) {
            isLoading = true
            val amount = amountText.toDoubleOrNull() ?: 0.0
            viewModel.fetchConversionRateWithAmount(apiKey, baseCurrency, targetCurrency, amount)
        }
    }

    // Handle conversion response
    LaunchedEffect(conversionResponse) {
        isLoading = false
        isInitialLoading = false
        conversionResponse?.conversionResult?.let { result ->
            convertedAmount = result.toString()

            // Hide the keyboard after conversion
            keyboardController?.hide()
        }
    }

    // Display loading spinner until initial data is fetched
    if (isInitialLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(50.dp)
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            )
            // Screen title
            Text(
                text = "Currency Converter",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Card for inputs and currency dropdowns
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Base currency dropdown
                    currencyList?.let {
                        CurrencyDropdown(
                            label = stringResource(R.string.base_currency),
                            selectedCurrency = baseCurrency,
                            onCurrencySelected = { baseCurrency = it },
                            currencyList = it
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Target currency dropdown
                    currencyList?.let {
                        CurrencyDropdown(
                            label = stringResource(R.string.target_currency),
                            selectedCurrency = targetCurrency,
                            onCurrencySelected = { targetCurrency = it },
                            currencyList = it
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount input field
                    CurrencyInputField(
                        label = "Amount",
                        value = amountText,
                        isNumeric = true,
                        onValueChange = { amountText = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Convert button
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (amountText.isNotBlank()) {
                        viewModel.fetchConversionRateWithAmount(apiKey, baseCurrency, targetCurrency, amount)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(stringResource(R.string.convertButton))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display message (success/error)
            message?.let {
                Text(
                    text = it,
                    color = if (it.startsWith("Error")) Color.Red else MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display conversion result
            ConversionResult(
                isLoading = isLoading,
                convertedAmount = convertedAmount,
                baseCurrency = baseCurrency,
                targetCurrency = targetCurrency
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Disclaimer text
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
}






