package com.strangenaut.networkclient.info.domain.usecase

import com.strangenaut.networkclient.info.domain.model.ConnectivityInfo
import com.strangenaut.networkclient.info.domain.repository.NetworkInfoRepository

class GetConnectivityInfo(private val repository: NetworkInfoRepository) {

    suspend operator fun invoke(): ConnectivityInfo {
        return repository.getConnectivityInfo()
    }
}