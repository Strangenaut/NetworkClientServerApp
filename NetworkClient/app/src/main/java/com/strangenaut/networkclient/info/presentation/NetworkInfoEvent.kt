package com.strangenaut.networkclient.info.presentation

sealed class NetworkInfoEvent {

    data object UpdateNetworkInfo : NetworkInfoEvent()

    data object StartUpdatingNetworkInfo : NetworkInfoEvent()
}