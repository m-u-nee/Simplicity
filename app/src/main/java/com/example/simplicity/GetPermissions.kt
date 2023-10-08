package com.example.simplicity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log



fun checkSettingsPermissions(context: Context?, settingsCanWrite: Boolean): Boolean {
    var settingsCanWrite = settingsCanWrite
    if (!settingsCanWrite) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            settingsCanWrite = Settings.System.canWrite(context)
            Log.d("TAG", "Can Write Settings: $settingsCanWrite")
            if (!settingsCanWrite) {
                // permission not granted navigate to permission screen
                openAndroidPermissionsMenu(context!!)
            }
        }
    }
    return settingsCanWrite
}

private fun openAndroidPermissionsMenu(context: Context) {
    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
    intent.data = Uri.parse("package:" + context.packageName)
    context.startActivity(intent)
}

