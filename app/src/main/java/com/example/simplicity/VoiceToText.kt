package com.example.simplicity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale



data class VoiceToTextParserState(
    val isSpeaking: Boolean = false,
    val spokenText: String = "",
    val error: String? = null
)

class VoiceToTextParser(
    private val app: Application
) : RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextParserState())

    val state: StateFlow<VoiceToTextParserState>
        get() = _state.asStateFlow()

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(app)

    fun startListening(languageCode: String) {
        // Clears the state
        _state.update { VoiceToTextParserState() }

        // If is not available shows the error
        if (!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update {
                it.copy(
                    error = "Speech recognition is not available"
                )
            }
        }

        // Creates an Intent for speech recognition in a specified language
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }

        // Sets the listener that will receive all the callbacks
        recognizer.setRecognitionListener(this)

        // Starts listening for speech
        recognizer.startListening(intent)

        // Indicates that speech recognition has started
        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopListening() {
        // Indicates that speech recognition has stopped
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }

        // Stops listening for speech
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        // Clears the error
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onEndOfSpeech() {
        // Indicates that speech recognition has stopped
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {
        // Gets recognition results
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { text ->
                _state.update {
                    it.copy(
                        spokenText = text
                    )
                }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit
}





class Speaker(private val context: Context) {
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                val englishLocale = Locale("it")
                tts?.language = englishLocale
            } else {
                Log.e("Speaker", "TextToSpeech initialization failed with status: $status")
            }
        }
    }

    fun speakText(text: String) {
        val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        if (result == TextToSpeech.ERROR) {
            Log.e("Speaker", "TextToSpeech speakText failed")
        }
    }

    fun shutdown() {
        tts?.shutdown()
        Log.d("Speaker", "TextToSpeech engine shutdown")
    }
}
