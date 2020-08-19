package com.example.sampletestraman.database

import androidx.room.*
import io.reactivex.Single


@Dao
interface PersonDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPerson(entity: Person)

    @Query("select * from person")
    fun getPersonList(): Single<List<Person>>

}