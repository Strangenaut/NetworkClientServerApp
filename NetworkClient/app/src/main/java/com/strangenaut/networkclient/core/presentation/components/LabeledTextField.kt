package com.strangenaut.networkclient.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    label: String,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val hintTextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    var textValue by remember {
        mutableStateOf(initialValue)
    }

    Column(modifier = modifier.padding(bottom = 8.dp)) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    onValueChange(it)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to Color.Transparent,
                    0.15f to Color.Transparent,
                    0.15f to MaterialTheme.colorScheme.onSurface,
                    0.90f to MaterialTheme.colorScheme.onSurface,
                    0.90f to Color.Transparent,
                    1.00f to Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = keyboardType
                ),
                modifier = Modifier.fillMaxWidth(0.75f)
            )
            if (textValue.isEmpty()) {
                Text(
                    text = hint,
                    style = hintTextStyle
                )
            }
        }
    }
}

@Preview
@Composable
private fun LabeledTextFieldPreview() {
    LabeledTextField(
        initialValue = "",
        label = "What is your email?",
        hint = "Enter email",
        onValueChange = {}
    )
}