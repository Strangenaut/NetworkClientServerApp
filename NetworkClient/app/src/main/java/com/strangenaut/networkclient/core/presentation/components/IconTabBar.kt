package com.strangenaut.networkclient.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.strangenaut.networkclient.core.navigation.model.IconTabBarItem
import com.strangenaut.networkclient.core.navigation.util.setScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Composable
fun IconTabBar(
    currentTabIndex: MutableStateFlow<Int>,
    iconTabBarItems: List<IconTabBarItem>,
    navController: NavHostController
) {
    val collectedState = currentTabIndex.collectAsState()
    val currentTabIndexRemember by remember {
        mutableStateOf(collectedState)
    }

    val navBarItemColors = NavigationBarItemDefaults.colors (
        indicatorColor = MaterialTheme.colorScheme.background,
    )

    Column {
        HorizontalLine(
            height = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.fillMaxWidth()
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxHeight(0.12f)
        ) {
            iconTabBarItems.forEachIndexed { index, tabBarItem ->
                val selected = currentTabIndexRemember.value == index

                NavigationBarItem(
                    colors = navBarItemColors,
                    selected = selected,
                    onClick = {
                        currentTabIndex.update {
                            index
                        }
                        navController.setScreen(tabBarItem.route)
                    },
                    icon = {
                        Icon(
                            imageVector = tabBarItem.icon,
                            tint = if (selected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.fillMaxHeight(0.5f),
                            contentDescription = tabBarItem.route
                        )
                    },
                    label = {}
                )
            }
        }
    }
}