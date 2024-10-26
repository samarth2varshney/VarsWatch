package com.example.varswatch.notification_module

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MusicState(
    val isPlaying: Boolean = false,
    val currentDuration: Long = 0,
    val title: String = "",
    val album: String = "",
    val artist: String = "",
    val albumArt: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val duration: Long = 0
):Parcelable
