package com.strangenaut.networkclient.info.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PermissionController(
    private val context: Context,
    private val requiredPermissions: Array<String>
) {

    private val _permissions = MutableStateFlow(mapOf<String, Boolean>())
    val permissions: StateFlow<Map<String, Boolean>> get() = _permissions

    init {
        updatePermissionsInfo()
    }

    @Composable
    fun MultiplePermissionsLauncher() {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                _permissions.update{
                    permissions
                }
            }
        )

        LaunchedEffect(key1 = true) {
            launcher.launch(requiredPermissions)
        }
    }

    private fun updatePermissionsInfo() {
        val permissions = mutableMapOf<String, Boolean>()
        requiredPermissions.forEach { permission ->
            permissions[permission] = hasRequiredPermission(context, permission)
        }

        _permissions.update {
            permissions
        }
    }

    companion object {

        fun hasRequiredPermission(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}