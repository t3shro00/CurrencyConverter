package com.example.currencyconverter.data.model

import com.google.gson.annotations.SerializedName

data class PairConversionResponse(
    val result: String,
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("target_code") val targetCode: String,
    @SerializedName("conversion_rate") val conversionRate: Double,
    // conversion_result is optional if the amount is provided
    @SerializedName("conversion_result") val conversionResult: Double? = null
)

data class CurrencyListResponse(
    val result: String,
    val supported_codes: List<List<String>> // List of pairs (currency code, currency name)
)
