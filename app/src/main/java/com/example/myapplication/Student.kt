package com.example.myapplication

class Student(n: String, age: Int) : Person(n, age){
    override val permission
    get() = "denied"
}