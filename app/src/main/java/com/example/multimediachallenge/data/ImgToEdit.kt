package com.example.multimediachallenge.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImgToEdit(val uri: Uri) : Parcelable
