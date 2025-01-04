package com.strangenaut.networkclient.core.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.strangenaut.networkclient.core.navigation.NavGraph
import com.strangenaut.networkclient.sender.presentation.PacketSenderViewModel
import com.strangenaut.networkclient.core.ui.theme.NetworkClientTheme
import com.strangenaut.networkclient.info.presentation.NetworkInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val packetSenderViewModel: PacketSenderViewModel by viewModels()
    private val networkInfoViewModel: NetworkInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            packetSenderViewModel.error.observe(this) { message ->
                if (message.isNotEmpty()) {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }

            NetworkClientTheme {
                NavGraph(
                    packetSenderViewModel = packetSenderViewModel,
                    networkInfoViewModel = networkInfoViewModel
                )
            }
        }
    }
}
