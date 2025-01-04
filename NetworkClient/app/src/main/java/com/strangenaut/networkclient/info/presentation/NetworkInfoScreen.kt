package com.strangenaut.networkclient.info.presentation

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.strangenaut.networkclient.R
import com.strangenaut.networkclient.info.presentation.components.LabeledInfo
import com.strangenaut.networkclient.info.presentation.components.PullToRefreshBox
import com.strangenaut.networkclient.info.presentation.util.PermissionController
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkInfoScreen(
    modifier: Modifier = Modifier,
    networkInfoState: StateFlow<NetworkInfoState>,
    onUpdateNetworkInfo: () -> Unit
) {
    val networkPermissionController = PermissionController(
        LocalContext.current,
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val permissions = networkPermissionController.permissions.collectAsState()

    if (permissions.value.any { !it.value }) {
        networkPermissionController.MultiplePermissionsLauncher()
    }

    val state by networkInfoState.collectAsState()

    val connectivityInfo = state.connectivityInfo
    val wifiInfo = state.wifiInfo
    val telephonyInfo = state.telephonyInfo

    PullToRefreshBox(
        isRefreshing = state.isUpdating,
        onRefresh = onUpdateNetworkInfo
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (permissions.value.any { !it.value }) {
                return@Column
            }

            LabeledInfo(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = stringResource(id = R.string.connectivity),
                iconPainter = painterResource(id = R.drawable.connectivity),
                description = "${stringResource(R.string.active_network_type)}: " +
                        "${connectivityInfo.activeNetworkType}\n" +
                        "${stringResource(R.string.all_network_types)}: " +
                        connectivityInfo.allNetworkTypes.joinToString(", ")
            )

            val configuredNetworks = mutableListOf<String>().apply {
                wifiInfo.configuredNetworks.forEach {
                    add("""
                    ${stringResource(R.string.network)} ${it.networkId}
                        SSID: ${it.ssid}
                        BSSID: ${it.bssid}
                        ${stringResource(R.string.priority)}: ${it.priority}
                    """.trimIndent()
                    )
                }
            }

            LabeledInfo(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = stringResource(id = R.string.wifi_network),
                iconPainter = painterResource(id = R.drawable.wifi),
                description = if (wifiInfo.noWifiConnection) stringResource(R.string.no_wifi) else
                    """
                IP: ${wifiInfo.ip}
                MAC: ${wifiInfo.mac}
                ${stringResource(R.string.link_speed)}: ${wifiInfo.linkSpeed}
                SSID: ${wifiInfo.ssid}
                BSSID: ${wifiInfo.bssid}
                RSSI: ${wifiInfo.rssi}
                DHCP: ${wifiInfo.dhcpInfo}
                ${stringResource(R.string.configured_networks)}: ${configuredNetworks.joinToString()}
            """.trimIndent()
            )

            LabeledInfo(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = stringResource(id = R.string.mobile_network),
                iconPainter = painterResource(id = R.drawable.mobile),
                description = """
                ${stringResource(R.string.data_state)}: ${telephonyInfo.dataState}
                ${stringResource(R.string.phone_type)}: ${telephonyInfo.phoneType}
                ${stringResource(R.string.network_type)}: ${telephonyInfo.networkType}
                ${stringResource(R.string.gsm_cell_location)}:
                    ${stringResource(R.string.cell_id)}: ${telephonyInfo.gsmCellLocation.cellId}
                    ${stringResource(R.string.lac)}: ${telephonyInfo.gsmCellLocation.lac}
                ${stringResource(R.string.mcc)}: ${telephonyInfo.mcc}
                ${stringResource(R.string.mnc)}: ${telephonyInfo.mnc}
                ${stringResource(R.string.location)}: ${telephonyInfo.location}
                ${stringResource(R.string.network_operator)}: ${telephonyInfo.networkOperator}
                ${stringResource(R.string.sim_operator)}: ${telephonyInfo.simOperator}
            """.trimIndent()
            )
        }
    }
}