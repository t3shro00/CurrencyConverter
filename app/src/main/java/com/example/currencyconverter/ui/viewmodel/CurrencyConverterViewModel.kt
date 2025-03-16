package com.example.currencyconverter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.api.RetrofitInstance
import com.example.currencyconverter.data.model.ExchangeRatesResponse
import com.example.currencyconverter.data.model.PairConversionResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Sealed class for UI states
sealed class UiState {
    object Loading : UiState()
    data class Success(val message: String? = null) : UiState()
    data class Error(val message: String) : UiState()
}

class CurrencyConverterViewModel : ViewModel() {

    private val _conversionRate = MutableStateFlow<PairConversionResponse?>(null)
    val conversionRate: StateFlow<PairConversionResponse?> get() = _conversionRate

    private val _currencyList = MutableStateFlow<Map<String, String>?>(null)
    val currencyList: StateFlow<Map<String, String>?> get() = _currencyList

    private val _exchangeRates = MutableStateFlow<ExchangeRatesResponse?>(null)
    val exchangeRates: StateFlow<ExchangeRatesResponse?> get() = _exchangeRates

    private val _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState: StateFlow<UiState> get() = _uiState

    /**
     * Fetch conversion rate for a given amount
     */
    fun fetchConversionRateWithAmount(apiKey: String, baseCurrency: String, targetCurrency: String, amount: Double) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = RetrofitInstance.api.getConversionRateWithAmount(apiKey, baseCurrency, targetCurrency, amount)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _conversionRate.value = it
                        _uiState.value =
                            UiState.Success("Conversion Result: ${it.conversionResult ?: "N/A"}")
                    } ?: run {
                        _uiState.value = UiState.Error("Error: Empty response body")
                    }
                } else {
                    handleApiError(response.code(), response.errorBody()?.string())
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    /**
     * Fetch available currency list
     */
    fun fetchCurrencyList(apiKey: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = RetrofitInstance.api.getCurrencyList(apiKey)
                if (response.isSuccessful) {
                    val currencyMap = response.body()?.supported_codes?.associate { it[0] to it[1] } ?: emptyMap()
                    _currencyList.value = currencyMap
                    _uiState.value = UiState.Success("Currency List Loaded")
                } else {
                    handleApiError(response.code(), response.errorBody()?.string())
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    /**
     * Fetch exchange rates for a specific currency
     */
    fun fetchExchangeRates(apiKey: String, currency: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = RetrofitInstance.api.getExchangeRates(apiKey, currency)
                if (response.isSuccessful) {
                    _exchangeRates.value = response.body()
                    _uiState.value = UiState.Success("Exchange rates fetched successfully")
                }
                else {
                    handleApiError(response.code(), response.errorBody()?.string())
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    /**
     * Helper method to handle API errors
     */
    private fun handleApiError(code: Int, errorBody: String?) {
        val errorMessage = "Error: Code $code, Message: ${errorBody ?: "Unknown error"}"
        _uiState.value = UiState.Error(errorMessage)
        Log.e("APIError", errorMessage)
    }

    /**
     * Helper method to handle exceptions
     */
    private fun handleException(e: Exception) {
        _uiState.value = UiState.Error("Error: ${e.localizedMessage}")
        Log.e("ExceptionError", "Error: ${e.message}", e)
    }
}
