package com.example.varswatch.notification_module

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MediaPlayerReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        NotificationModule.mediaAction(intent.action!!)

    }

}