package com.strangenaut.networkclient.info.domain.repository

import com.strangenaut.networkclient.info.domain.model.ConnectivityInfo
import com.strangenaut.networkclient.info.domain.model.TelephonyInfo
import com.strangenaut.networkclient.info.domain.model.WifiInfo

interface NetworkInfoRepository {

    suspend fun getConnectivityInfo(): ConnectivityInfo

    suspend fun getWifiInfo(): WifiInfo

    suspend fun getTelephonyInfo(): TelephonyInfo
}