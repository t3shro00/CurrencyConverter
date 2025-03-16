package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.api.ExchangeRatesApi
import com.example.currencyconverter.data.api.RetrofitInstance
import com.example.currencyconverter.data.model.CurrencyListResponse
import com.example.currencyconverter.data.model.PairConversionResponse
import retrofit2.Response
import java.io.IOException

class CurrencyRepository(
    private val api: ExchangeRatesApi = RetrofitInstance.api
) {

    // Get the conversion rate between two currencies
    suspend fun getConversionRate(
        apiKey: String, base: String, target: String
    ): Result<PairConversionResponse> {
        return try {
            val response = api.getConversionRate(apiKey, base, target)
            checkResponse(response)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get the conversion rate between two currencies with an amount
    suspend fun getConversionRateWithAmount(
        apiKey: String,
        base: String,
        target: String,
        amount: Double
    ): Result<PairConversionResponse> {
        return try {
            val response = api.getConversionRateWithAmount(apiKey, base, target, amount)
            checkResponse(response)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetch list of supported currencies
    suspend fun getCurrencyList(apiKey: String): Result<CurrencyListResponse> {
        return try {
            val response = api.getCurrencyList(apiKey)
            checkResponse(response)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Helper function to check API responses
    private  fun <T> checkResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response body"))
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            Result.failure(Exception("API Error: ${response.code()}, Message: $errorBody"))
        }
    }
}
