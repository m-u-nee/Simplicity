package com.example.simplicity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject


fun convertJsonToFunctionCall(context: Context, jsonString: String) {
    try {
        val jsonObject = JSONObject(jsonString)

        if (!jsonObject.has("function_call")) {
            Toast.makeText(
                context,
                "Function call details were not provided in the input.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val functionCallObject = jsonObject.getJSONObject("function_call")

        if (!functionCallObject.has("name")) {
            Toast.makeText(context, "Function name not provided in the input.", Toast.LENGTH_LONG).show()
            return
        }

        val functionName = functionCallObject.getString("name")

        if (!functionCallObject.has("arguments")) {
            Toast.makeText(context, "Arguments for the function were not provided.", Toast.LENGTH_LONG).show()
            return
        }

        val arguments = functionCallObject.getString("arguments")

        when (functionName) {
            "sendMessage" -> {
                val argumentsObject = JSONObject(arguments)
                val contact = argumentsObject.optString("contact", "")
                val message = argumentsObject.optString("message", "")
                sendMessage(context, contact, message)
            }
            "openPhone" -> {
                val argumentsObject = JSONObject(arguments)
                val numberToCall = argumentsObject.optString("numberToCall", "")
                openPhone(context, numberToCall)
            }
            // Add more cases for other functions if needed
            "openMaps" -> {
                val argumentsObject = JSONObject(arguments)
                val location = argumentsObject.optString("location", "")
                openMaps(context, location)
            }
            "openSettings" -> {
                val argumentsObject = JSONObject(arguments)
                val page = argumentsObject.optString("page", "")
                openSettings(context, page)
            }
            "raiseOrLowerVolume" -> {
                val argumentsObject = JSONObject(arguments)
                val raiseOrLower = argumentsObject.optString("raiseOrLower", "")
                raiseOrLowerVolume(context, raiseOrLower)
            }
            "raiseOrLowerBrightness" -> {
                val argumentsObject = JSONObject(arguments)
                val raiseOrLower = argumentsObject.optString("raiseOrLower", "")
                raiseOrLowerBrightness(context, raiseOrLower)
            }
            "askUserQuestion" -> {
                val argumentsObject = JSONObject(arguments)
                val question = argumentsObject.optString("question", "")
                askUserQuestion(context, question)
            }
            "searchGoogle" -> {
                val argumentsObject = JSONObject(arguments)
                val query = argumentsObject.optString("query", "")
                searchGoogle(context, query)
            }
            "functionalityNotAvailable" -> {
                val argumentsObject = JSONObject(arguments)
                val explanation = argumentsObject.optString("explanation", "")
                functionalityNotAvailable(context, explanation)
            }
            "openApp" -> {
                val argumentsObject = JSONObject(arguments)
                val appToOpen = argumentsObject.optString("appToOpen", "")
                openApp(context,appToOpen)
            }
            "contactViaWhatsApp" -> {
                val argumentsObject = JSONObject(arguments)
                val contact = argumentsObject.optString("contact", "")
                val callOrMessage = argumentsObject.optString("callOrMessage", "")
                val message = argumentsObject.optString("message", "")
                contactViaWhatsApp(context,contact,callOrMessage,message)
            }
            else -> {

            }

        }

    } catch (jsonException: JSONException) {
        // Handle errors from incorrect JSON formatting
        Toast.makeText(context, "Failed to parse JSON due to invalid format.", Toast.LENGTH_LONG).show()
    } catch (exception: Exception) {
        // Handle any other unexpected exceptions
        Toast.makeText(context, "An unexpected error occurred.", Toast.LENGTH_LONG).show()
    }
}


