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

        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity3::class.java))
        }

        binding.button7.setOnClickListener {
            val email = binding.textView8.text.toString()
            val password = binding.textView10.text.toString()
            if(RegistrationUtil.validationRegistrationInput(email,password,password))
                auth.createUserWithEmailAndPassword(binding.textView8.text.toString(),binding.textView10.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        startActivity(Intent(this,MainActivity3::class.java))
                        finish()
                        Toast.makeText(this,"Successfull",Toast.LENGTH_LONG).show()
                    }
                }
            else
                Toast.makeText(this,"enter valid credentials",Toast.LENGTH_LONG).show()
        }

        binding.textView6.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        }

    }

}