package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.ui.screens.PairConversionScreen
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.viewmodel.PairConversionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                PairConversionScreen(
                    viewModel = PairConversionViewModel(),
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyConverterTheme {
        PairConversionScreen(
            viewModel = PairConversionViewModel(),
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )

    }
}