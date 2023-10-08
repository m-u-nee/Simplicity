package com.example.simplicity

import android.Manifest
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.simplicity.ui.theme.SimplicityTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {

    private val voiceToText by lazy {
        VoiceToTextParser(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimplicityTheme() {
                MobileAds.initialize(this) {}
                MyAppContent(this@MainActivity, voiceToText)
            }

        }
    }
}

@Composable
fun MyAppContent(activity: ComponentActivity, voiceToText: VoiceToTextParser) {
    val ads = true
    val settingsCanWrite: Boolean = Settings.System.canWrite(activity)
    checkSettingsPermissions(activity, settingsCanWrite)
    var canRecord by remember { mutableStateOf(false) }


    // Creates a permission request
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )

    LaunchedEffect(key1 = recordAudioLauncher) {
        // Launches the permission request
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
    val state by voiceToText.state.collectAsState()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (ads){AdaptiveBanner(modifier = Modifier.fillMaxWidth())}
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly, // distribute the space evenly
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize() // fill the available space
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PhoneActionButton(activity)
                        MessageActionButton(activity)
                    }

                    // The new Row of buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MapsActionButton(activity)
                        SettingsActionButton(activity)
                    }

                }

            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                VoiceActionButton(activity, canRecord, state, voiceToText)
            }
        }
    }
}







