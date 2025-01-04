package com.strangenaut.networkclient.info.domain.usecase

import com.strangenaut.networkclient.info.domain.model.TelephonyInfo
import com.strangenaut.networkclient.info.domain.repository.NetworkInfoRepository

class GetTelephonyInfo(private val repository: NetworkInfoRepository) {

    suspend operator fun invoke(): TelephonyInfo {
        return repository.getTelephonyInfo()
    }
}