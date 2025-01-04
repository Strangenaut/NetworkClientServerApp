package com.strangenaut.networkclient.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.strangenaut.networkclient.core.presentation.components.IconTabBar
import com.strangenaut.networkclient.core.navigation.model.IconTabBarItem
import com.strangenaut.networkclient.info.presentation.NetworkInfoEvent
import com.strangenaut.networkclient.info.presentation.NetworkInfoScreen
import com.strangenaut.networkclient.info.presentation.NetworkInfoViewModel
import com.strangenaut.networkclient.sender.presentation.PacketSenderEvent
import com.strangenaut.networkclient.sender.presentation.PacketSenderScreen
import com.strangenaut.networkclient.sender.presentation.PacketSenderViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun NavGraph(
    packetSenderViewModel: PacketSenderViewModel,
    networkInfoViewModel: NetworkInfoViewModel
) {
    val packetSenderTab = IconTabBarItem(
        route = Screen.PacketSender.route,
        icon = Icons.AutoMirrored.Filled.Send
    )
    val networkInfoTab = IconTabBarItem(
        route = Screen.NetworkInfo.route,
        icon = Icons.Outlined.Info,
    )

    val tabBarItems = listOf(packetSenderTab, networkInfoTab)

    val navController = rememberNavController()

    val startDestinationIndex = maxOf(
        a = tabBarItems.indexOf(
            tabBarItems.find {
                it.route == navController.currentDestination?.route
            }
        ),
        b = 0
    )
    val currentTabIndex = MutableStateFlow(startDestinationIndex)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            IconTabBar(
                currentTabIndex = currentTabIndex,
                iconTabBarItems = tabBarItems,
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.PacketSender.route
        ) {
            composable(Screen.PacketSender.route) {
                PacketSenderScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    packetSenderState = packetSenderViewModel.state,
                    onUpdateConfiguration = { ip, port, numberOfPackets, iat, message ->
                        packetSenderViewModel.onEvent(
                            PacketSenderEvent.UpdateConfiguration(
                                ip, port, numberOfPackets, iat, message
                            )
                        )
                    },
                    onSubmitTcpPacket = {
                        packetSenderViewModel.onEvent(PacketSenderEvent.SendTcpPacket)
                    },
                    onSubmitUdpPacket = {
                        packetSenderViewModel.onEvent(PacketSenderEvent.SendUdpPacket)
                    }
                )
            }
            composable(Screen.NetworkInfo.route) {
                networkInfoViewModel.onEvent(NetworkInfoEvent.StartUpdatingNetworkInfo)

                NetworkInfoScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    networkInfoState =  networkInfoViewModel.state,
                    onUpdateNetworkInfo = {
                        networkInfoViewModel.onEvent(NetworkInfoEvent.UpdateNetworkInfo)
                    }
                )
            }
        }
    }
}