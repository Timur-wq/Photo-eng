package com.example.myapplication

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tunes")
class Avatar {
    @PrimaryKey
    var _id: Int
    var photo: ByteArray
    var title: String

    @Ignore
    constructor(){
        _id = 11
        photo = ByteArray(0)
        title = ""
    }

    constructor(_id: Int, photo: ByteArray, title: String){
        this._id = _id
        this.photo = photo
        this.title = title
    }

}