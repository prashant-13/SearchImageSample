package com.healthcoco.safeplace.network

import com.example.sampletestsearchimage.network.CustomResponse
import io.reactivex.Single
import retrofit2.http.*


interface NetworkService {

    @GET(Endpoints.SEARCH)
    fun searchImageByName(
            @Query("q") versionCode: String?
    ): Single<CustomResponse>

}