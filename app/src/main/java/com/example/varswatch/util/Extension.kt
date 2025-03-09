package com.example.varswatch.util

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import com.example.varswatch.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.permissionx.guolindev.PermissionX

fun AppCompatActivity.getNotificationPermission(success:()->Unit){

    PermissionX.init(this).permissions(android.Manifest.permission.POST_NOTIFICATIONS)
        .request{allGranted,_,_ ->

            if(allGranted){
                success()
            }else{
                Toast.makeText(this, "Notification permission required to control player from it", Toast.LENGTH_SHORT).show()
            }

        }
}

open class VarsFragment:Fragment(){
    private val signIn: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onSignInResult)

    protected val auth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        checkUserStatus()
    }

    protected fun signOut(){
        AuthUI.getInstance().signOut(requireContext()).addOnSuccessListener {
            checkUserStatus()
        }
    }

    private fun checkUserStatus(){
        if (auth.currentUser == null) {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                ))
                .build()
            signIn.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            Log.d(TAG, "Sign in successful!")
            refreshActivity()
        } else {
            Toast.makeText(requireContext(), "You have to sign in to use this app", Toast.LENGTH_LONG).show()

            val response = result.idpResponse
            if (response == null) {
                Log.w(TAG, "Sign in canceled")
            } else {
                Log.w(TAG, "Sign in error", response.error)
            }
        }
    }

    private fun refreshActivity() {
        requireActivity().recreate()
    }

}