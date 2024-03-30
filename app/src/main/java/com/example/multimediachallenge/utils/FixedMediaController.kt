package com.example.multimediachallenge.utils

import android.content.Context
import android.widget.MediaController

class FixedMediaController(context: Context) : MediaController(context) {

    override fun hide() {}
}