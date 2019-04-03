package com.example.mytrainstation.http

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface RestApi {

    @GET("getAllStationsXML")
    fun getStations(): Deferred<Response<ResponseBody>>

    @GET("getCurrentTrainsXML")
    fun getRunningTrains(): Deferred<Response<ResponseBody>>

    @GET("getStationDataByCodeXML")
    fun getStationData(@Query("StationCode") stationCode: String): Deferred<Response<ResponseBody>>

    @GET("getTrainMovementsXML")
    fun getTrainMovement(@Query("TrainId") trainId: String, @Query("TrainDate") trainDate: String): Deferred<Response<ResponseBody>>
}