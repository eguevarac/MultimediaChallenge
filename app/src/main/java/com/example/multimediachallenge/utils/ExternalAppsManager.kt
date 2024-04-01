package com.example.multimediachallenge.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object ExternalAppsManager {

    fun lookingForInformationAtWikipedia(context: Context, query: String) {
        Dialogs.queryToWikipediaDialog(context)

        val wikipediaUrl = "https://es.wikipedia.org/wiki/$query"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(wikipediaUrl)
            `package` = "com.android.chrome"
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun lookingForAddressAtMaps(context: Context, address: String) {

        val uri = Uri.parse("geo:0,0?q=$address")

        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        }
    }
}