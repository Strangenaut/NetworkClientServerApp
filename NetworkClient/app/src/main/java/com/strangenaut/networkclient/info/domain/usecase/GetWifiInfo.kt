package com.strangenaut.networkclient.info.domain.usecase

import com.strangenaut.networkclient.info.domain.model.WifiInfo
import com.strangenaut.networkclient.info.domain.repository.NetworkInfoRepository

class GetWifiInfo(private val repository: NetworkInfoRepository) {

    suspend operator fun invoke(): WifiInfo {
        return repository.getWifiInfo()
    }
}