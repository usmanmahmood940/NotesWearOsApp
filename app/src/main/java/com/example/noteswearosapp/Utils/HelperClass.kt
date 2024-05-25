package com.example.noteswearosapp.Utils

import java.util.Random

object HelperClass {
    fun generateRandomStringWithTime(): String {
        val timestamp = System.currentTimeMillis()
        val randomString = generateRandomString(6)
        return "$timestamp-$randomString"
    }

    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random()
        return (1..length)
            .map { charset[random.nextInt(charset.length)] }
            .joinToString("")
    }

}