package com.example.sampletestsearchimage.database.entity

import java.io.Serializable

data class ImageResponse(
        var id: String,
        var title: String,
        var datetime: Long,
        var images: List<ImagesData>) : Serializable
