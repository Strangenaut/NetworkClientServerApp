package com.strangenaut.networkclient.sender.data.repository

import android.content.SharedPreferences
import com.strangenaut.networkclient.sender.domain.model.Configuration
import com.strangenaut.networkclient.sender.domain.model.Message
import com.strangenaut.networkclient.sender.domain.repository.ConfigurationRepository
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ConfigurationRepository {

    override fun saveConfiguration(configuration: Configuration) {
        sharedPreferences.edit().apply {
            putString("ip", configuration.ip)
            putString("port", configuration.port)
            putInt("number_of_packets", configuration.numberOfPackets)
            putLong("iat", configuration.iat)
            putString("message", configuration.message?.payload)

            apply()
        }
    }

    override fun loadConfiguration(): Configuration {
        return Configuration(
            sharedPreferences.getString("ip", "") ?: "",
            sharedPreferences.getString("port", "") ?: "",
            sharedPreferences.getInt("number_of_packets", 1),
            sharedPreferences.getLong("iat", 0),
            sharedPreferences.getString("message", "")?.let { Message(it) }
        )
    }
}