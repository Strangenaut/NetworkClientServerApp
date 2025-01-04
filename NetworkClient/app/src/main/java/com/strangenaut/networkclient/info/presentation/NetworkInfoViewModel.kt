package com.strangenaut.networkclient.info.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strangenaut.networkclient.info.domain.usecase.NetworkInfoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NetworkInfoViewModel @Inject constructor(
    private val networkInfoUseCases: NetworkInfoUseCases
) : ViewModel() {

    private lateinit var _state: MutableStateFlow<NetworkInfoState>
    lateinit var state: StateFlow<NetworkInfoState>

    init {
        viewModelScope.launch(Dispatchers.Default) {
            _state = MutableStateFlow(getNetworkInfo())
            state = _state.asStateFlow()
        }
    }

    fun onEvent(event: NetworkInfoEvent) {
        viewModelScope.launch {
            executeEvent(event)
        }
    }

    private suspend fun executeEvent(event: NetworkInfoEvent) {
        when (event) {
            NetworkInfoEvent.UpdateNetworkInfo -> {
                _state.update {
                    _state.value.copy(isUpdating = true)
                }

                withContext(Dispatchers.Default) {
                    _state.update {
                        getNetworkInfo()
                    }
                }
            }
            NetworkInfoEvent.StartUpdatingNetworkInfo -> {
                withContext(Dispatchers.Default) {
                    while(true) {
                        val networkInfoState = getNetworkInfo()
                        _state.update {
                            networkInfoState.copy(isUpdating = _state.value.isUpdating)
                        }
                        delay(UPDATING_DELAY_TIME_MILLIS)
                    }
                }
            }
        }
    }

    private suspend fun getNetworkInfo(): NetworkInfoState {
        return NetworkInfoState(
            connectivityInfo = networkInfoUseCases.getConnectivityInfo(),
            wifiInfo = networkInfoUseCases.getWifiInfo(),
            telephonyInfo = networkInfoUseCases.getTelephonyInfo(),
        )
    }

    companion object {

        private const val UPDATING_DELAY_TIME_MILLIS = 3000L
    }
}