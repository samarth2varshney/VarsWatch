package com.example.drive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.drive.databinding.ActivitySignInEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class createUser : AppCompatActivity() {
    private lateinit var binding: ActivitySignInEmailBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.button7.setOnClickListener {
            val email = binding.etName2.text.toString()
            val password = binding.etClass.text.toString()
            if(RegistrationUtil.validationRegistrationInput(email,password,password))
                auth.createUserWithEmailAndPassword(binding.etName2.text.toString(),binding.etClass.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        startActivity(Intent(this,MainActivity2::class.java))
                        Toast.makeText(this,"Successfull",Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

}