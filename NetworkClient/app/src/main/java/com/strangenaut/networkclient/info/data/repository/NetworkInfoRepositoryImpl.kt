@file:Suppress("DEPRECATION")

package com.strangenaut.networkclient.info.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import androidx.core.app.ActivityCompat
import com.strangenaut.networkclient.NetworkClientApp
import com.strangenaut.networkclient.R
import com.strangenaut.networkclient.info.data.service.LocationService
import com.strangenaut.networkclient.info.domain.model.ConfiguredNetworkInfo
import com.strangenaut.networkclient.info.domain.model.ConnectivityInfo
import com.strangenaut.networkclient.info.domain.model.GsmCellLocationInfo
import com.strangenaut.networkclient.info.domain.model.TelephonyInfo
import com.strangenaut.networkclient.info.domain.model.WifiInfo
import com.strangenaut.networkclient.info.domain.repository.NetworkInfoRepository
import com.strangenaut.networkclient.info.domain.util.toIpString
import java.net.UnknownHostException

class NetworkInfoRepositoryImpl(
    private val app: NetworkClientApp,
    private val connectivityManager: ConnectivityManager,
    private val wifiManager: WifiManager,
    private val telephonyManager: TelephonyManager,
    private val locationService: LocationService
) : NetworkInfoRepository {

    private var allNetworks = listOf<String>()

    override suspend fun getConnectivityInfo(): ConnectivityInfo {
        val activeNetwork = connectivityManager.activeNetwork
        val activeNetworkCapabilities = activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }

        val activeType = when {
            activeNetworkCapabilities == null -> app.getString(R.string.none)
            activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                app.getString(R.string.wifi)
            activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                app.getString(R.string.cellular)
            activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                app.getString(R.string.ethernet)
            else -> app.getString(R.string.unknown)
        }

        updateAllNetworks()

        return ConnectivityInfo(activeType, allNetworks)
    }

    @SuppressLint("HardwareIds")
    override suspend fun getWifiInfo(): WifiInfo {
        updateAllNetworks()

        if (ActivityCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_WIFI_STATE
            ) != PackageManager.PERMISSION_GRANTED ||
            "Wifi" !in allNetworks
        ) {
            return WifiInfo(noWifiConnection = true)
        }

        val connectionInfo = wifiManager.connectionInfo

        val configuredNetworks = mutableListOf<ConfiguredNetworkInfo>().apply {
            wifiManager.configuredNetworks.forEach {
                this.add(
                    ConfiguredNetworkInfo(
                        networkId = it.networkId.toString(),
                        ssid = it.SSID,
                        bssid = it.BSSID,
                        priority = it.priority.toString()
                    )
                )
            }
        }

        return WifiInfo(
            ip = connectionInfo.ipAddress.toIpString(),
            mac = connectionInfo.macAddress,
            linkSpeed = "${connectionInfo.linkSpeed} ${app.getString(R.string.mbps)}",
            ssid = connectionInfo.ssid,
            bssid = connectionInfo.bssid,
            rssi = "${connectionInfo.rssi} ${app.getString(R.string.dbm)}",
            dhcpInfo = wifiManager.dhcpInfo.toString(),
            configuredNetworks = configuredNetworks
        )
    }

    override suspend fun getTelephonyInfo(): TelephonyInfo {
        if (ActivityCompat.checkSelfPermission(
                app,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return TelephonyInfo(noTelephonyInfo = true)
        }

        val dataState = when (telephonyManager.dataState) {
            TelephonyManager.DATA_DISCONNECTED ->
                app.getString(R.string.disconnected)
            TelephonyManager.DATA_CONNECTING ->
                app.getString(R.string.connecting)
            TelephonyManager.DATA_CONNECTED ->
                app.getString(R.string.connected)
            TelephonyManager.DATA_SUSPENDED ->
                app.getString(R.string.suspended)
            TelephonyManager.DATA_DISCONNECTING ->
                app.getString(R.string.disconnecting)
            TelephonyManager.DATA_HANDOVER_IN_PROGRESS ->
                app.getString(R.string.handover_in_progress)
            else -> app.getString(R.string.unknown)
        }

        val phoneType = when (telephonyManager.phoneType) {
            TelephonyManager.PHONE_TYPE_NONE -> app.getString(R.string.none)
            TelephonyManager.PHONE_TYPE_GSM -> "GSM"
            TelephonyManager.PHONE_TYPE_CDMA -> "CDMA"
            TelephonyManager.PHONE_TYPE_SIP -> "SIP"
            else -> app.getString(R.string.unknown)
        }

        val networkType = when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
            TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> "CDMA - EvDo rev. 0"
            TelephonyManager.NETWORK_TYPE_EVDO_A -> "CDMA - EvDo rev. A"
            TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
            TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
            TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_IDEN -> "IDEN"
            TelephonyManager.NETWORK_TYPE_EVDO_B -> "CDMA - EvDo rev. B"
            TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
            TelephonyManager.NETWORK_TYPE_EHRPD -> "CDMA - eHRPD"
            TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPAP"
            TelephonyManager.NETWORK_TYPE_GSM -> "GSM"
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "TD_SCDMA"
            TelephonyManager.NETWORK_TYPE_IWLAN -> "IWLAN"
            TelephonyManager.NETWORK_TYPE_NR -> "NR"
            else -> app.getString(R.string.unknown)
        }

        var lac = app.getString(R.string.none)
        var cellId = app.getString(R.string.none)
        val gsmCellLocation = telephonyManager.cellLocation
        if (gsmCellLocation is GsmCellLocation) {
            lac = gsmCellLocation.lac.toString()
            cellId = gsmCellLocation.cid.toString()
        }

        var mcc = app.getString(R.string.none)
        var mnc = app.getString(R.string.none)

        val networkOperator = telephonyManager.networkOperator
        if (!networkOperator.isNullOrEmpty() && networkOperator.length >= 5) {
            mcc = networkOperator.substring(0..2)
            mnc = networkOperator.substring(3..4)
        } else if (!networkOperator.isNullOrEmpty()) {
            mcc = networkOperator.substring(0..2)
        }

        val location = if (mcc != app.getString(R.string.none) && mnc != app.getString(R.string.none)) {
            getLocation(mcc.toInt(), mnc.toInt(), lac.toInt(), cellId.toInt())
        } else {
            app.getString(R.string.none)
        }

        return TelephonyInfo(
            dataState = dataState,
            phoneType = phoneType,
            networkType = networkType,
            gsmCellLocation = GsmCellLocationInfo(cellId, lac),
            mcc = mcc,
            mnc = mnc,
            location = location,
            networkOperator = telephonyManager.networkOperatorName.ifEmpty { app.getString(R.string.none) },
            simOperator = telephonyManager.simOperatorName.ifEmpty { app.getString(R.string.none) },
        )
    }

    private fun updateAllNetworks() {
        allNetworks = connectivityManager.allNetworks.mapNotNull { network ->
            val caps = connectivityManager.getNetworkCapabilities(network) ?: return@mapNotNull null

            when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                    app.getString(R.string.wifi)
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                    app.getString(R.string.cellular)
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                    app.getString(R.string.ethernet)
                else -> null
            }
        }
    }

    private suspend fun getLocation(mcc: Int, mnc: Int, lac: Int, cellId: Int) = try {
        val locationResponse = locationService.getLocation(mcc, mnc, lac, cellId)
        val html = locationResponse.string()

        val latRegex = """lat = ([\d.\-]+);""".toRegex()
        val lngRegex = """lng = ([\d.\-]+);""".toRegex()

        val latitudeValue = latRegex.find(html)?.groupValues?.get(1)?.toDoubleOrNull()
        val longitudeValue = lngRegex.find(html)?.groupValues?.get(1)?.toDoubleOrNull()

        val latitude = if (latitudeValue != null)
            formatDegree(latitudeValue, 'N', 'S')
        else
            app.getString(R.string.none)

        val longitude = if (longitudeValue != null)
            formatDegree(longitudeValue, 'E', 'W')
        else
            app.getString(R.string.none)

        "$latitude, $longitude"
    } catch (e: UnknownHostException) {
        app.getString(R.string.failed_to_connect)
    }

    private fun formatDegree(degree: Double, positivePostfix: Char, negativePostfix: Char): String {
        return if (degree < 0) {
            "${-degree}° $negativePostfix"
        } else {
            "${degree}° $positivePostfix"
        }
    }
}