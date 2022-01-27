package com.example.myapplication

class Teacher(n: String, age: Int) : Person(n, age){
    override val permission: String
    get() = "accepted"

}