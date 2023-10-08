package com.example.simplicity

import android.view.SoundEffectConstants
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun PhoneActionButton(activity: ComponentActivity) {
    FloatingActionButton(
        onClick = {
            openPhone(context = activity)
                  },
        modifier = Modifier.size(140.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Phone",
                fontSize = 22.sp
            )
            Icon(
                imageVector = Icons.Rounded.Phone,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun MessageActionButton(activity: ComponentActivity) {
    FloatingActionButton(
        onClick = { sendMessage(context = activity) },
        modifier = Modifier.size(140.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Messages",
                fontSize = 22.sp
            )
            Icon(
                imageVector = Icons.Rounded.Message,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun MapsActionButton(activity: ComponentActivity) {
    FloatingActionButton(
        onClick = { openMaps(context = activity) }, // You need to implement the openMaps function
        modifier = Modifier.size(140.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Maps",
                fontSize = 22.sp
            )
            Icon(
                imageVector = Icons.Rounded.Map, // Assuming you have a Map icon in your Icons.Rounded object
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun SettingsActionButton(activity: ComponentActivity) {
    FloatingActionButton(
        onClick = { openSettings(context = activity) }, // You need to implement the openSettings function
        modifier = Modifier.size(140.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Settings",
                fontSize = 22.sp
            )
            Icon(
                imageVector = Icons.Rounded.Settings, // Assuming you have a Settings icon in your Icons.Rounded object
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun VoiceActionButton(activity: ComponentActivity, canRecord: Boolean, state: VoiceToTextParserState, voiceToText: VoiceToTextParser) {
    val lifecycleScope = rememberCoroutineScope()
    val showLoading = remember { mutableStateOf(false) }
    var hasSentData = false
    FloatingActionButton(
        onClick = {
            if (canRecord) {
                if (!state.isSpeaking) {
                    voiceToText.startListening("it")
                } else {
                    voiceToText.stopListening()
                    lifecycleScope.launch {
                        voiceToText.state.collect { newState ->
                            if (!newState.isSpeaking && newState.spokenText.isNotEmpty() && !hasSentData) {
                                // Set hasSentData flag to true immediately after the condition passes
                                hasSentData = true

                                launch {
                                    showLoading.value = true
                                    sendDataAndGetResponse(activity, newState.spokenText)
                                    NonCancellable.cancel() // Stop collecting after sending the recognized text to the server
                                    showLoading.value = false
                                }
                            }
                        }


                    }
                }
            }
        },
        modifier = Modifier.size(325.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (state.isSpeaking) "Speak..." else state.spokenText.ifEmpty { "Tap to record" },
                textAlign = TextAlign.Center, // This will set the alignment of the text to center
                style = MaterialTheme.typography.titleMedium,
                fontSize = 30.sp,
                modifier = Modifier.fillMaxWidth() // This ensures that the text takes up the entire width, allowing for the center alignment to work effectively
            )
            if (showLoading.value) {
                Box(Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator() // Show loading symbol when loading
                }
            } else {
                Icon(
                    imageVector = if (state.isSpeaking) Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}

