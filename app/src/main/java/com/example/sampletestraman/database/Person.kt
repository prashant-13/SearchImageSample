package com.example.sampletestraman.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "person")
data class Person(

        @PrimaryKey
        @ColumnInfo(name = "person_id")
        var personId: Long,

        @ColumnInfo(name = "first_name")
        var firstName: String,

        @ColumnInfo(name = "last_name")
        var lastName: String,

        @ColumnInfo(name = "phone_number")
        var phoneNumber: String,

        @ColumnInfo(name = "city")
        var city: String,

        @ColumnInfo(name = "state")
        var state: String,

        @ColumnInfo(name = "pincode")
        var pincode: String) : Serializable
