package com.example.varswatch.util

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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