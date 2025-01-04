package com.strangenaut.networkclient.info.domain.model

data class TelephonyInfo(
    val noTelephonyInfo: Boolean = false,
    val dataState: String = "",
    val phoneType: String = "",
    val networkType: String = "",
    val gsmCellLocation: GsmCellLocationInfo = GsmCellLocationInfo(),
    val mcc: String = "",
    val mnc: String = "",
    val location: String = "",
    val networkOperator: String = "",
    val simOperator: String = ""
)