package com.strangenaut.networkclient.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun HorizontalLine(
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(color)) {
        Spacer(modifier = Modifier.height(height))
    }
}