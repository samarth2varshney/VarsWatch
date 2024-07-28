package com.example.drive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.drive.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    val auth = Firebase.auth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity3::class.java))
        }

        binding.textView5.setOnClickListener {
            startActivity(Intent(this,createUser::class.java))
            finish()
        }

        binding.button7.setOnClickListener {
            val email = binding.textView8.text.toString()
            val password = binding.textView10.text.toString()

            if(RegistrationUtil.validationRegistrationInput(email,password,password))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this,MainActivity3::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            else
                Toast.makeText(this,"enter valid credentials",Toast.LENGTH_LONG).show()
        }

    }
}