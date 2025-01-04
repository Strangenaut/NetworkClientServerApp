package com.strangenaut.networkclient.info.data.service

import com.strangenaut.networkclient.info.data.util.LOCATION_FILE_PATH
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    @GET(LOCATION_FILE_PATH)
    suspend fun getLocation(
        @Query("mcc") mcc: Int,
        @Query("mnc") mnc: Int,
        @Query("lac") lac: Int,
        @Query("cid") cellId: Int
    ): ResponseBody
}