package com.example.currencyconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

    // Handle text field click to toggle the dropdown
    TextField(
        value = textState.value,
        onValueChange = {
            textState.value = it
            expanded.value = true
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray
        ),
        readOnly = true // The dropdown will handle input, not the user typing
    )

    // Dropdown menu
    ExposedDropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        currencyList.forEach { (currencyCode, currencyName) ->
            // Each dropdown item
            Text(
                text = "$currencyCode - $currencyName",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // When an item is clicked, update the selected currency and close the dropdown
                        onCurrencySelected(currencyCode)
                        expanded.value = false
                    }
                    .padding(8.dp)
            )
        }
    }
}


@Composable
fun ExposedDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    if (expanded) {
        // A simple dropdown menu with a column layout
        Card(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),  // Correct elevation
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    } else {
        // If not expanded, dismiss the dropdown
        onDismissRequest()
    }
}
