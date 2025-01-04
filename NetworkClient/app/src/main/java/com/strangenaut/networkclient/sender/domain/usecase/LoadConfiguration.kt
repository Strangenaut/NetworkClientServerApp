package com.strangenaut.networkclient.sender.domain.usecase

import com.strangenaut.networkclient.sender.domain.model.Configuration
import com.strangenaut.networkclient.sender.domain.repository.ConfigurationRepository

class LoadConfiguration(private val repository: ConfigurationRepository) {

    operator fun invoke(): Configuration {
        return repository.loadConfiguration()
    }
}