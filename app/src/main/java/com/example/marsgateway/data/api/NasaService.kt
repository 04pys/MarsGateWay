package com.example.marsgateway.data.api

import com.example.marsgateway.model.MarsRoverResponseModel
import retrofit2.Call
import com.example.marsgateway.model.PictureData
import okhttp3.ResponseBody
import retrofit2.http.*

interface NasaService {

    // insung
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getPhotos(@Query("earth_date") earthDate : String, @Query("page") page : Int, @Query("api_key") apiKey : String)
    : Call<MarsRoverResponseModel>

    //윤수
    @GET("planetary/apod")
    fun getPicture(@Query("api_key") api_key: String): Call<PictureData>


    //junsang
    @FormUrlEncoded
    @POST("/layout/embed/image/m20weather/")
    fun getMarsWeather(@FieldMap temperture : Map<String, String>): Call<ResponseBody>
}