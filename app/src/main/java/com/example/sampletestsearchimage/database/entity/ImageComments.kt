package com.example.sampletestsearchimage.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "image_comments")
data class ImageComments(

        @PrimaryKey
        @ColumnInfo(name = "unique_id")
        var uniqueId: Long,

        @ColumnInfo(name = "image_id")
        var imageId: String,

        @ColumnInfo(name = "datetime")
        var datetime: Long,

        @ColumnInfo(name = "comment")
        var comment: String) : Serializable
