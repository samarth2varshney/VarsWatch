package com.example.varswatch.util

object Query {
    fun getQuery(
        query:String
    ):String{
        val regex = Regex("\\b\\w+\\b")
        val matches = regex.findAll(query)
        return matches.map { it.value }.joinToString("+")
    }
}