package com.example.drive

import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Test

class createUserTest{

    @Test
    fun `empty username return false`(){
        val result = RegistrationUtil.validationRegistrationInput(
            "",
            "123",
            "123"
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `incorrect confirm password return false`(){
        val result = RegistrationUtil.validationRegistrationInput(
            "samarth",
            "1232455",
            "123"
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty password return false`(){
        val result = RegistrationUtil.validationRegistrationInput(
            "samarth",
            "",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `less than 2 digit password return false`(){
        val result = RegistrationUtil.validationRegistrationInput(
            "samarth",
            "12asd",
            "12asd"
        )
        Truth.assertThat(result).isTrue()
    }

}