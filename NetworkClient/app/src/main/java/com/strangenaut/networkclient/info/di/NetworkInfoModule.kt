package com.strangenaut.networkclient.info.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import com.strangenaut.networkclient.NetworkClientApp
import com.strangenaut.networkclient.info.data.repository.NetworkInfoRepositoryImpl
import com.strangenaut.networkclient.info.data.service.LocationService
import com.strangenaut.networkclient.info.data.util.CELL_PHONE_TRACKERS_SERVICE
import com.strangenaut.networkclient.info.data.util.TIME_OUT_SECONDS
import com.strangenaut.networkclient.info.domain.repository.NetworkInfoRepository
import com.strangenaut.networkclient.info.domain.usecase.GetConnectivityInfo
import com.strangenaut.networkclient.info.domain.usecase.GetTelephonyInfo
import com.strangenaut.networkclient.info.domain.usecase.GetWifiInfo
import com.strangenaut.networkclient.info.domain.usecase.NetworkInfoUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkInfoModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(app: NetworkClientApp): ConnectivityManager {
        return app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideWifiManager(app: NetworkClientApp): WifiManager {
        return app.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    @Provides
    @Singleton
    fun provideTelephonyManager(app: NetworkClientApp): TelephonyManager {
        return app.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideLocationService(client: OkHttpClient): LocationService {
        return Retrofit.Builder()
            .baseUrl(CELL_PHONE_TRACKERS_SERVICE)
            .client(client)
            .build()
            .create(LocationService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkInfoRepository(
        app: NetworkClientApp,
        locationService: LocationService,
        connectivityManager: ConnectivityManager,
        wifiManager: WifiManager,
        telephonyManager: TelephonyManager
    ): NetworkInfoRepository {
        return NetworkInfoRepositoryImpl(
            app,
            connectivityManager,
            wifiManager,
            telephonyManager,
            locationService
        )
    }

    @Provides
    @Singleton
    fun provideNetworkInfoUseCases(repository: NetworkInfoRepository): NetworkInfoUseCases {
        return NetworkInfoUseCases(
            getConnectivityInfo = GetConnectivityInfo(repository),
            getWifiInfo = GetWifiInfo(repository),
            getTelephonyInfo = GetTelephonyInfo(repository)
        )
    }
}