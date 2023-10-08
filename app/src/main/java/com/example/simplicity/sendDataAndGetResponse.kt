package com.example.simplicity

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket


suspend fun sendDataAndGetResponse(activity: ComponentActivity, spokenText: String) {
    withContext(Dispatchers.IO) {
        try {
            val socket = Socket("10.169.138.177", 1111)
            val outputStream = socket.getOutputStream()
            Log.d("SendSocket", "State spoken text: $spokenText")
            outputStream.write(spokenText.toByteArray())

            // Read the response from the server
            val inputStream = socket.getInputStream()
            val responseBytes = inputStream.readBytes()
            val response = responseBytes.toString(Charsets.UTF_8)
            Log.d("Response", response) // Log the response

            convertJsonToFunctionCall(context = activity, jsonString = response)
            socket.close()
        } catch (e: Exception) {
            // Handle the exception here
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, "Failed to connect to server", Toast.LENGTH_LONG).show()
            }
        }
    }
}