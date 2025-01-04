package com.strangenaut.networkclient.sender.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.strangenaut.networkclient.R
import com.strangenaut.networkclient.sender.domain.model.Message
import com.strangenaut.networkclient.core.presentation.components.IconTextButton
import com.strangenaut.networkclient.core.presentation.components.LabeledTextField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PacketSenderScreen(
    modifier: Modifier = Modifier,
    packetSenderState: StateFlow<PacketSenderState>,
    onUpdateConfiguration: (
        ip: String,
        port: String,
        numberOfPackets: Int,
        iat: Long,
        message: Message
    ) -> Unit,
    onSubmitTcpPacket: () -> Unit,
    onSubmitUdpPacket: () -> Unit
) {
    val collectedState = packetSenderState.collectAsState()
    val state by remember {
        mutableStateOf(collectedState)
    }
    
    var ip by remember {
        mutableStateOf(state.value.configuration?.ip ?: "")
    }
    var port by remember {
        mutableStateOf(state.value.configuration?.port ?: "")
    }
    var numberOfPackets by remember {
        mutableIntStateOf(state.value.configuration?.numberOfPackets ?: 1)
    }
    var iat by remember {
        mutableLongStateOf(state.value.configuration?.iat ?: 0)
    }
    var message by remember {
        mutableStateOf(state.value.configuration?.message ?: Message())
    }

    val anyIsNotDigit = { input: String ->
        input.any { character -> !character.isDigit() }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        LabeledTextField(
            initialValue = ip,
            label = stringResource(R.string.receiver_ip),
            hint = stringResource(R.string.enter_address),
            keyboardType = KeyboardType.Number,
            onValueChange = {
                ip = it
                onUpdateConfiguration(ip, port, numberOfPackets, iat, message)
            }
        )
        LabeledTextField(
            initialValue = port,
            label = stringResource(R.string.receiver_port),
            hint = stringResource(R.string.enter_port),
            keyboardType = KeyboardType.Number,
            onValueChange = {
                port = it
                onUpdateConfiguration(ip, port, numberOfPackets, iat, message)
            }
        )
        LabeledTextField(
            initialValue = numberOfPackets.toString(),
            label = stringResource(R.string.number_of_packets),
            hint = stringResource(R.string.enter_number_of_packets),
            keyboardType = KeyboardType.Number,
            onValueChange = {
                if (anyIsNotDigit(it) || it.isEmpty()) {
                    return@LabeledTextField
                }

                numberOfPackets = it.toInt()
                onUpdateConfiguration(ip, port, numberOfPackets, iat, message)
            }
        )
        LabeledTextField(
            initialValue = iat.toString(),
            label = stringResource(R.string.inter_arrival_time),
            hint = stringResource(R.string.enter_iat),
            keyboardType = KeyboardType.Number,
            onValueChange = {
                if (anyIsNotDigit(it) || it.isEmpty()) {
                    return@LabeledTextField
                }

                iat = it.toLong()
                onUpdateConfiguration(ip, port, numberOfPackets, iat, message)
            }
        )
        LabeledTextField(
            initialValue = message.payload,
            label = stringResource(R.string.message),
            hint = stringResource(R.string.enter_message),
            onValueChange = {
                message = Message(payload = it)
                onUpdateConfiguration(ip, port, numberOfPackets, iat, message)
            }
        )

        IconTextButton(
            text = stringResource(R.string.send_tcp),
            icon = Icons.AutoMirrored.Filled.Send,
            onClick = {
                onSubmitTcpPacket()
            }
        )
        IconTextButton(
            text = stringResource(R.string.send_udp),
            icon = Icons.AutoMirrored.Filled.Send,
            onClick = {
                onSubmitUdpPacket()
            }
        )
    }
}

@Preview
@Composable
private fun NetworkScreenPreview() {
    PacketSenderScreen(
        modifier = Modifier.background(Color.White),
        packetSenderState = MutableStateFlow(PacketSenderState()),
        onUpdateConfiguration = { _, _, _, _, _-> },
        onSubmitTcpPacket = { },
        onSubmitUdpPacket = { }
    )
}