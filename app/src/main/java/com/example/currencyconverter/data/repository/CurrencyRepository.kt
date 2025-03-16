package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.api.ExchangeRatesApi
import com.example.currencyconverter.data.model.CurrencyListResponse
import com.example.currencyconverter.data.model.ExchangeRatesResponse
import com.example.currencyconverter.data.model.PairConversionResponse
import retrofit2.Response

class CurrencyRepository(private val api: ExchangeRatesApi) {

    // Fetch conversion rate for a currency pair with an amount
    suspend fun getConversionRateWithAmount(
        apiKey: String, baseCurrency: String, targetCurrency: String, amount: Double
    ): Response<PairConversionResponse> {
        return api.getConversionRateWithAmount(apiKey, baseCurrency, targetCurrency, amount)
    }

    // Fetch available currency list
    suspend fun getCurrencyList(apiKey: String): Response<CurrencyListResponse> {
        return api.getCurrencyList(apiKey)
    }

    // Fetch exchange rates for a specific currency
    suspend fun getExchangeRates(apiKey: String, currency: String): Response<ExchangeRatesResponse> {
        return api.getExchangeRates(apiKey, currency)
    }
}
