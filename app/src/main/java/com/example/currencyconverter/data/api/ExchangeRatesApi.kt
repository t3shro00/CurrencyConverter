package com.example.currencyconverter.data.api

import com.example.currencyconverter.data.model.CurrencyListResponse
import com.example.currencyconverter.data.model.PairConversionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRatesApi {
    // Pair conversion without an amount
    @GET("v6/{api_key}/pair/{base_currency}/{target_currency}")
    suspend fun getConversionRate(
        @Path("api_key") apiKey: String,
        @Path("base_currency") baseCurrency: String,
        @Path("target_currency") targetCurrency: String
    ): Response<PairConversionResponse> // 🔹 FIXED

    // Pair conversion with an amount
    @GET("v6/{api_key}/pair/{base_currency}/{target_currency}/{amount}")
    suspend fun getConversionRateWithAmount(
        @Path("api_key") apiKey: String,
        @Path("base_currency") baseCurrency: String,
        @Path("target_currency") targetCurrency: String,
        @Path("amount") amount: Double
    ): Response<PairConversionResponse> // 🔹 FIXED

    // Get the list of supported currencies
    @GET("v6/{api_key}/codes")
    suspend fun getCurrencyList(@Path("api_key") apiKey: String): Response<CurrencyListResponse>
}
