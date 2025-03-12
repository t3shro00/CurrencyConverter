package com.example.currencyconverter.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    label: String,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencyList: Map<String, String>
) {
    // State to manage dropdown expanded state and text field value
    val expanded = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue(selectedCurrency)) }

    // State for search query
    val searchQuery = remember { mutableStateOf("") }

    // Filtered currency list based on search query
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
            .clickable { expanded.value = true }, // Toggling dropdown on click
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ),
        readOnly = true // The dropdown will handle input, not the user typing
    )

    // Show the dropdown menu only when there are items in filteredCurrencyList
    if (expanded.value && filteredCurrencyList.isNotEmpty()) {
        // Adding a fade-in/out effect with animations
        AnimatedVisibility(
            visible = expanded.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                // Search TextField
                TextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )

                // Scrollable list of currencies
                LazyColumn {
                    items(filteredCurrencyList.toList()) { (currencyCode, currencyName) ->
                        Text(
                            text = "$currencyCode - $currencyName",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // When an item is clicked, update the selected currency and close the dropdown
                                    onCurrencySelected(currencyCode)
                                    textState.value = TextFieldValue(currencyCode) // Update the selected currency
                                    expanded.value = false // Close the dropdown
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

