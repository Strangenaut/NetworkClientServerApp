package com.strangenaut.networkclient.core.navigation

sealed class Screen(val route: String) {

    data object PacketSender : Screen("packet_sender")

    data object NetworkInfo : Screen("network_info")
}