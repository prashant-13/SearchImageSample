package com.example.sampletestsearchimage.database

import androidx.room.*
import com.example.sampletestsearchimage.database.entity.ImageComments
import io.reactivex.Single


@Dao
interface SampleDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addComment(entity: ImageComments)

    @Query("select * from image_comments where image_id = :imageId")
    fun getCommentsList(imageId: String): Single<List<ImageComments>>

}