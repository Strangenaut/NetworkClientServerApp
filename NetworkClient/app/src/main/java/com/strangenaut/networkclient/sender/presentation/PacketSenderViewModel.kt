package com.strangenaut.networkclient.sender.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strangenaut.networkclient.sender.domain.model.Configuration
import com.strangenaut.networkclient.sender.domain.model.Message
import com.strangenaut.networkclient.sender.domain.usecase.PacketSenderUseCases
import com.strangenaut.networkclient.sender.domain.util.randomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PacketSenderViewModel @Inject constructor(
  private val useCases: PacketSenderUseCases
) : ViewModel() {

    private enum class PacketType {
        TCP,
        UDP
    }

    private val _state: MutableStateFlow<PacketSenderState>
    val state: StateFlow<PacketSenderState>

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        val configuration = useCases.loadConfiguration()
        val packetSenderState = PacketSenderState(configuration)

        _state = MutableStateFlow(packetSenderState)
        state = _state.asStateFlow()
    }

    fun onEvent(event: PacketSenderEvent) {
        viewModelScope.launch {
            try {
                executeEvent(event)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    private suspend fun executeEvent(event: PacketSenderEvent) {
        when (event) {
            is PacketSenderEvent.UpdateConfiguration -> {
                val configuration = Configuration(
                    event.ip,
                    event.port,
                    event.numberOfPackets,
                    event.iat,
                    event.message
                )

                _state.update {
                    _state.value.copy(configuration = configuration)
                }

                withContext(Dispatchers.IO) {
                    useCases.saveConfiguration(configuration)
                }
            }
            is PacketSenderEvent.SendTcpPacket -> {
                withContext(Dispatchers.IO) {
                    sendPacket(PacketType.TCP)
                }
            }
            is PacketSenderEvent.SendUdpPacket -> {
                withContext(Dispatchers.IO) {
                    sendPacket(PacketType.UDP)
                }
            }
        }
    }

    private suspend fun sendPacket(type: PacketType) {
        val configuration = _state.value.configuration ?: return

        val ip = configuration.ip
        val port = configuration.port
        val numberOfPackets = configuration.numberOfPackets
        val iat = configuration.iat
        var message = configuration.message

        val isMessageNullOrEmpty = message?.payload == null || message.payload.isEmpty()

        repeat(numberOfPackets) {
            if (isMessageNullOrEmpty) {
                message = Message(randomString(RANDOM_STRING_LENGTH))
            }

            if (type == PacketType.TCP) {
                useCases.sendTcpPacket(ip, port, message!!)
            } else {
                useCases.sendUdpPacket(ip, port, message!!)
            }

            Thread.sleep(iat)
        }
    }

    companion object {

        private const val RANDOM_STRING_LENGTH = 16
    }
}