package com.example.currencyconverter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.api.RetrofitInstance
import com.example.currencyconverter.data.model.ExchangeRatesResponse
import com.example.currencyconverter.data.model.PairConversionResponse
import kotlinx.coroutines.launch

class PairConversionViewModel : ViewModel() {

    private val _conversionRate = MutableLiveData<PairConversionResponse?>(null)
    val conversionRate: LiveData<PairConversionResponse?> get() = _conversionRate

    private val _currencyList = MutableLiveData<Map<String, String>?>()
    val currencyList: LiveData<Map<String, String>?> get() = _currencyList

    private val _exchangeRates = MutableLiveData<ExchangeRatesResponse?>()
    val exchangeRates: LiveData<ExchangeRatesResponse?> get() = _exchangeRates


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message



    // Fetch conversion rate with an amount
    fun fetchConversionRateWithAmount(
        apiKey: String,
        baseCurrency: String,
        targetCurrency: String,
        amount: Double
    ) {
        viewModelScope.launch {
            _message.postValue("Loading...") // Placed inside the coroutine

            try {
                val response = RetrofitInstance.api.getConversionRateWithAmount(
                    apiKey,
                    baseCurrency,
                    targetCurrency,
                    amount
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _conversionRate.postValue(responseBody)
                        _message.postValue("Conversion Result: ${responseBody.conversionResult ?: "N/A"}")
                    } else {
                        _message.postValue("Error: Empty response body")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _message.postValue("Failed to fetch conversion rate. Code: ${response.code()}, Message: $errorMessage")
                    Log.e("ConversionError", "Failed with response: $errorMessage")
                }
            } catch (e: Exception) {
                _message.postValue("Error: ${e.localizedMessage}")
                Log.e("ConversionError", "Error fetching conversion rate: ${e.message}")
            }
        }
    }




    // Fetch currency list
    fun fetchCurrencyList(apiKey: String) {
        viewModelScope.launch {
            _message.postValue("Loading...") // Placed inside the coroutine

            try {
                val response = RetrofitInstance.api.getCurrencyList(apiKey)
                Log.d("CurrencyListDebug", "Response: $response")

                if (response.isSuccessful) {
                    val currencyList = response.body()?.supported_codes ?: emptyList()
                    Log.d("CurrencyListDebug", "Currency List: $currencyList")

                    val currencyMap = mutableMapOf<String, String>()
                    currencyList.forEach { currency ->
                        val code = currency[0]
                        val name = currency[1]
                        currencyMap[code] = name
                    }

                    _currencyList.postValue(currencyMap)
                    _message.postValue("Currency List Loaded")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _message.postValue("Failed to load currency list. Code: ${response.code()}, Message: $errorMessage")
                    Log.e("CurrencyListError", "Failed with response: $errorMessage")
                }

            } catch (e: Exception) {
                _message.postValue("Error: ${e.localizedMessage}")
                Log.e("CurrencyListError", "Error loading currency list: ${e.message}")
            }
        }
    }

    // Inside PairConversionViewModel


    fun fetchExchangeRates(apiKey: String, currency: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getExchangeRates(apiKey, currency)
                if (response.isSuccessful) {
                    _exchangeRates.postValue(response.body())
                    _errorMessage.postValue("") // Reset error message if successful
                } else {
                    _errorMessage.postValue("Failed to fetch exchange rates: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching exchange rates: ${e.localizedMessage}")
            }
        }
    }

}