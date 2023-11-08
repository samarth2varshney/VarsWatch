package com.example.drive

import org.junit.Assert.*
import org.junit.Test

class QueryTest {

    @Test
    fun `test getQuery with regular alphanumeric query`() {
        val inputQuery = "This is a test query"
        val expected = "This+is+a+test+query"
        val result = Query.getQuery(inputQuery)
        assertEquals(expected, result)
    }

    @Test
    fun `test getQuery with query containing special characters`() {
        val inputQuery = "How's @ the weather today?"
        val expected = "How+s+the+weather+today"
        val result = Query.getQuery(inputQuery)
        assertEquals(expected, result)
    }

    @Test
    fun `test getQuery with numbers in the query`() {
        val inputQuery = "Get 5 apples and 10 oranges"
        val expected = "Get+5+apples+and+10+oranges"
        val result = Query.getQuery(inputQuery)
        assertEquals(expected, result)
    }

    @Test
    fun `test getQuery with empty query`() {
        val inputQuery = ""
        val expected = ""
        val result = Query.getQuery(inputQuery)
        assertEquals(expected, result)
    }

    @Test
    fun `test getQuery with query containing only one word`() {
        val inputQuery = "SingleWord"
        val expected = "SingleWord"
        val result = Query.getQuery(inputQuery)
        assertEquals(expected, result)
    }

    @Test
    fun `test getQuery with numbers and full stop in the query`() {
        val inputQuery = "Get 5 apples and 10 oranges."
        val expected = "Get+5+apples+and+10+oranges"
        val result = Query.getQuery(inputQuery)
        assertEquals(expected, result)
    }
}