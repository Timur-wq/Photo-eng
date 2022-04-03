package com.example.myapplication

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

//класс, объект которого загружается в базу данных и хранит регистрационные данные пользователя
@IgnoreExtraProperties
class AppUser(var name: String = "", var name2: String = "", var email: String = "", var password: String = "", var imgUri: String = "") {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "name2" to name2,
            "email" to email,
            "imgUri" to imgUri
        )
    }
}