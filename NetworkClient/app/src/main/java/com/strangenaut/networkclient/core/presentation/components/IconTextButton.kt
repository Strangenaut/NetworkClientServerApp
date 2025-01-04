package com.strangenaut.networkclient.core.presentation.components

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.strangenaut.networkclient.core.ui.theme.SurfaceShape

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    widthPercentage: Float = 0.75f,
    onClick: () -> Unit
) {
    var iconSize by remember {
        mutableIntStateOf(0)
    }

    val screenDensity = Resources.getSystem().displayMetrics.density

    Button(
        onClick = onClick,
        shape = SurfaceShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .fillMaxWidth(widthPercentage)
            .padding(vertical = 16.dp)
            .height(50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.onSizeChanged {
                    iconSize = (it.width / screenDensity).toInt()
                }
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(iconSize.dp))
        }
    }
}

@Preview
@Composable
private fun IconTextButtonPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconTextButton(
            text = "List of participants",
            icon = Icons.AutoMirrored.Filled.List,
            onClick = {}
        )
    }
}