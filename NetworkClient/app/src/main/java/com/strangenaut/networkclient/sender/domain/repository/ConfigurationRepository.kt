package com.strangenaut.networkclient.sender.domain.repository

import com.strangenaut.networkclient.sender.domain.model.Configuration

interface ConfigurationRepository {

    fun saveConfiguration(configuration: Configuration)

    fun loadConfiguration(): Configuration
}