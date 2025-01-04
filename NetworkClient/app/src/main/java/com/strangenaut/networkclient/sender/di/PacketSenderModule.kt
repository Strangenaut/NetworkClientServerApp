package com.strangenaut.networkclient.sender.di

import android.content.Context
import android.content.SharedPreferences
import com.strangenaut.networkclient.NetworkClientApp
import com.strangenaut.networkclient.sender.data.repository.ConfigurationRepositoryImpl
import com.strangenaut.networkclient.sender.data.repository.NetworkRepositoryImpl
import com.strangenaut.networkclient.sender.domain.repository.ConfigurationRepository
import com.strangenaut.networkclient.sender.domain.repository.NetworkRepository
import com.strangenaut.networkclient.sender.domain.usecase.LoadConfiguration
import com.strangenaut.networkclient.sender.domain.usecase.PacketSenderUseCases
import com.strangenaut.networkclient.sender.domain.usecase.SaveConfiguration
import com.strangenaut.networkclient.sender.domain.usecase.SendTcpPacket
import com.strangenaut.networkclient.sender.domain.usecase.SendUdpPacket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PacketSenderModule {

    @Provides
    @Singleton
    fun provideConfigurationSharedPreferences(app: NetworkClientApp): SharedPreferences {
        return app.getSharedPreferences("configuration", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideConfigurationRepository(
        sharedPreferences: SharedPreferences
    ): ConfigurationRepository {
        return ConfigurationRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(): NetworkRepository {
        return NetworkRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePacketSenderUseCases(
        configurationRepository: ConfigurationRepository,
        networkRepository: NetworkRepository
    ): PacketSenderUseCases {
        return PacketSenderUseCases(
            loadConfiguration = LoadConfiguration(configurationRepository),
            saveConfiguration = SaveConfiguration(configurationRepository),
            sendTcpPacket = SendTcpPacket(networkRepository),
            sendUdpPacket = SendUdpPacket(networkRepository)
        )
    }
}