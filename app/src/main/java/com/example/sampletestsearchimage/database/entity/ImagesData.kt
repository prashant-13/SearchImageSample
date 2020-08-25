package com.example.sampletestsearchimage.database.entity

import java.io.Serializable

data class ImagesData(

        var id: String,
        var title: String,
        var datetime: Long,
        var link: String) : Serializable
