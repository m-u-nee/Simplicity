package com.example.simplicity

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast




fun sendMessage(context: Context, phoneNumber: String = "", message: String = "") {
    val uri = Uri.parse("smsto:$phoneNumber")
    val intent = Intent(Intent.ACTION_SENDTO, uri)

    if (phoneNumber.isNotEmpty()) {
        intent.putExtra("address", phoneNumber)
    }

    if (message.isNotEmpty()) {
        intent.putExtra("sms_body", message)
    }

    context.startActivity(intent)
}


fun searchOnYouTube(context: Context, searchName: String) {
    try {
        val intent = Intent(Intent.ACTION_SEARCH).apply {
            `package` = "com.google.android.youtube"
            putExtra("query", searchName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val activity = context as Activity
        activity.runOnUiThread {
            Toast.makeText(context, "Can't open youtube.", Toast.LENGTH_SHORT).show()
        }
    }
}






fun openApp(context: Context, appName: String) {
    // Mapping of app names to package names
    val appToPackageMap = mapOf(
        "Instagram" to "com.instagram.android",
        "Facebook" to "com.facebook.katana",
        "Twitter" to "com.twitter.android",
        "YouTube" to "com.google.android.youtube",
        "Snapchat" to "com.snapchat.android",
        "WhatsApp" to "com.whatsapp",
        "LinkedIn" to "com.linkedin.android",
        "Pinterest" to "com.pinterest",
        "Spotify" to "com.spotify.music",
        "Uber" to "com.ubercab",
        "Netflix" to "com.netflix.mediaclient",
        "Amazon Shopping" to "com.amazon.mShop.android.shopping"
        // add other apps here...
    )

    // Get the package name from the map using the app name
    val packageName = appToPackageMap[appName]

    val packageManager = context.packageManager
    var intent: Intent? = null

    if (packageName != null) { // If the package name was found
        try {
            intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.addCategory(Intent.CATEGORY_DEFAULT)
        } catch (e: Exception) {
            // Handle the situation when the package name not found
        }

        if (intent != null) {
            context.startActivity(intent)
        } else {
            // Handle the situation when the app is not installed
            val activity = context as Activity
            activity.runOnUiThread {
                Toast.makeText(context, "$appName is not installed on this device.", Toast.LENGTH_SHORT).show()
            }
        }
    } else { // If the package name was not found
        val activity = context as Activity
        activity.runOnUiThread {
            Toast.makeText(context, "App name not found in our list.", Toast.LENGTH_SHORT).show()
        }
    }
}


fun openPhone(context: Context, numberToCall: String = "") {
    if (numberToCall.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$numberToCall")
        context.startActivity(intent)
    } else {
        val intent = Intent(Intent.ACTION_DIAL)
        context.startActivity(intent)
    }
}

fun openMaps(context: Context, location: String = "") {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("geo:0,0?q=$location")
    context.startActivity(intent)
}

fun openSettings(context: Context, settingsOption: String = "") {
    val intent = when (settingsOption) {
        "wifi" -> Intent(Settings.ACTION_WIFI_SETTINGS)
        "bluetooth" -> Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        "volume" -> Intent(Settings.ACTION_SOUND_SETTINGS)
        else -> Intent(Settings.ACTION_SETTINGS)
    }
    context.startActivity(intent)
}

fun raiseOrLowerVolume(context: Context, raiseOrLower: String) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val delta = (maxVolume * 0.25).toInt()

    if (raiseOrLower == "raise") {
        if (currentVolume + delta <= maxVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + delta, 0)
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        }

    } else if (raiseOrLower == "lower") {
        if (currentVolume - delta >= 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - delta, 0)
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        }
    }
}




fun raiseOrLowerBrightness(context: Context, raiseOrLower: String) {
    val window = (context as Activity).window
    val layoutParams = window.attributes
    val currentBrightness = layoutParams.screenBrightness
    val delta = 0.25f // 25% of max brightness

    if (raiseOrLower == "raise") {
        if (currentBrightness + delta <= 1f) {
            layoutParams.screenBrightness = currentBrightness + delta
        } else {
            layoutParams.screenBrightness = 1f
        }
    } else if (raiseOrLower == "lower") {
        if (currentBrightness - delta >= 0f) {
            layoutParams.screenBrightness = currentBrightness - delta
        } else {
            layoutParams.screenBrightness = 0f
        }
    }
    window.attributes = layoutParams
}


fun changeWifiStatus(context: Context, targetStatus: Boolean){
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    wifiManager.isWifiEnabled = targetStatus
}


fun askUserQuestion(context: Context, question: String) {
    //val speaker = Speaker(context)
    //speaker.speakText(question)

    // Create a Toast with the question
    val activity = context as Activity
    activity.runOnUiThread {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(context)

        // Set the message, title, and button
        builder.setMessage(question)
            .setTitle("Question")
            .setPositiveButton("OK") { dialog, _ ->
                // User clicked OK button
                dialog.dismiss()
            }

        // Create and show the AlertDialog
        builder.create().show()
    }

    Log.d("AskUserQuestion", "Question: $question")
    //speaker.shutdown()

}

fun functionalityNotAvailable(context: Context, explanation: String) {

    // Create a Toast with the question
    val activity = context as Activity
    activity.runOnUiThread {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(context)

        // Set the message, title, and button
        builder.setMessage(explanation)
            .setTitle("Question")
            .setPositiveButton("OK") { dialog, _ ->
                // User clicked OK button
                dialog.dismiss()
            }

        // Create and show the AlertDialog
        builder.create().show()
    }



}


fun searchGoogle(context: Context, query: String = "") {
    val url = if (query.isNotEmpty()) {
        "https://www.google.com/search?q=$query"
    } else {
        "https://www.google.com"
    }
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    context.startActivity(intent)
}



fun contactViaWhatsApp(context: Context, contact: String, callOrMessage: String, message: String? = null) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setPackage("com.whatsapp")

    when (callOrMessage.toLowerCase()) {
        "call" -> {
            // For a call, the URI is 'tel:' followed by the phone number.
            intent.data = Uri.parse("tel:+393807755752")
        }
        "message" -> {
            // For a message, the URI is 'https://wa.me/' followed by the phone number.
            // The text of the message is appended as a URL parameter.
            val url = "https://wa.me/+393807755752" + if (message != null) "?text=$message" else ""
            intent.data = Uri.parse(url)
        }
        else -> {
            throw IllegalArgumentException("callOrMessage must be either 'call' or 'message'")
        }
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}