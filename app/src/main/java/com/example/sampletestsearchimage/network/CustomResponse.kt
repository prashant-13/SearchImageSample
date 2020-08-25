package com.example.sampletestsearchimage.network

import com.google.gson.annotations.Expose

data class CustomResponse(
        @Expose
        var data: Array<Any>,
        @Expose
        var success: String,
        @Expose
        var status: Int)