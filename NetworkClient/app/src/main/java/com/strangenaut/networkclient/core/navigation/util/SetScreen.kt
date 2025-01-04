package com.strangenaut.networkclient.core.navigation.util

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

fun NavHostController.setScreen(route: String) {
    val currentDestination = this.currentDestination?.route ?: ""
    navigate(route) {
        clearNavigationStack(currentDestination)
    }
}

private fun NavOptionsBuilder.clearNavigationStack(route: String) {
    popUpTo(route) {
        inclusive = true
        saveState = true
    }
}