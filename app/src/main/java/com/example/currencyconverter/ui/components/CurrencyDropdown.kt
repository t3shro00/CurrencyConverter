package com.example.currencyconverter.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    label: String,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencyList: Map<String, String>,
    modifier: Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue(selectedCurrency)) }
    val searchQuery = remember { mutableStateOf("") }

    // Filtered list based on search query
    val filteredCurrencyList = currencyList.filter {
        it.value.contains(searchQuery.value, ignoreCase = true) ||
                it.key.contains(searchQuery.value, ignoreCase = true)
    }

    // Handle text field click to toggle the dropdown
    TextField(
        value = textState.value,
        onValueChange = {
            textState.value = it
            expanded.value = true // Open dropdown when typing
        },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded.value = true }, // Toggle dropdown

        readOnly = true
    )

    // Display dropdown menu if expanded and list is not empty
    if (expanded.value && filteredCurrencyList.isNotEmpty()) {
        AnimatedVisibility(
            visible = expanded.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                // Search TextField inside dropdown
                TextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = { Text(stringResource(R.string.search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),

                )

                // Scrollable list of filtered currencies
                LazyColumn {
                    items(filteredCurrencyList.toList()) { (currencyCode, currencyName) ->
                        Text(
                            text = "$currencyCode - $currencyName",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCurrencySelected(currencyCode)
                                    textState.value =
                                        TextFieldValue(currencyCode) // Update selected currency
                                    expanded.value = false // Close dropdown
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
