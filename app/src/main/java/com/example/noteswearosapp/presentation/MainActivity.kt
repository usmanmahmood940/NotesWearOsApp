/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.noteswearosapp.presentation

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.contentcapture.ContentCaptureManager.Companion.isEnabled
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.noteswearosapp.presentation.screens.AddEditNotesScreen
import com.example.noteswearosapp.presentation.screens.NotesListScreen
import com.example.noteswearosapp.presentation.screens.Screens
import com.example.noteswearosapp.presentation.theme.NotesWearOsAppTheme
import com.example.noteswearosapp.viewModels.AddEditNotesViewModel
import com.google.mlkit.nl.translate.TranslateLanguage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var  recognitionIntent: Intent
    lateinit var speechRecognizer: SpeechRecognizer

    private val viewModel: AddEditNotesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp("Android")
        }
    }

    fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(speechRecognitionListener)
        recognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        val supportedLanguages = arrayOf(TranslateLanguage.ENGLISH, TranslateLanguage.HINDI, TranslateLanguage.GERMAN)
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, supportedLanguages.joinToString(","))
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

    }
    fun startListening() {
        try {
            speechRecognizer.startListening(recognitionIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d("Error", "No voice recognition")
            e.printStackTrace()
        }
    }

    val speechRecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.d("SpeechRecognition", "Error: 2")

        }

        override fun onBeginningOfSpeech() {
            Log.d("SpeechRecognition", "Error: 1")

        }

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            Log.d("SpeechRecognition", "Error: 3")

        }

        override fun onError(error: Int) {
            Log.d("SpeechRecognition", "Error: $error")
        }

        override fun onResults(results: Bundle?) {
            Log.d("SpeechRecognition", "Error: 4")

            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.get(0)?.let {
                viewModel.noteContent.value = it
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d("SpeechRecognition", "Error: 5")

        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }
}

@Composable
fun WearApp(greetingName: String) {
    NotesWearOsAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            App()
        }
    }
}

@Composable
fun App() {
    val navController = rememberSwipeDismissableNavController()
    val context = LocalContext.current

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screens.NotesListScreen.name
    ) {
        composable(route = Screens.NotesListScreen.name) {
            NotesListScreen(
                onNoteClick = {
                    // onNoteClick
                    Toast.makeText(context, "Note clicked", Toast.LENGTH_SHORT).show()
                },
                onAddNoteClick = {
                    navController.navigate(Screens.AddNoteScreen.name)
                }
            )
        }
        composable(route = Screens.AddNoteScreen.name) {
            // AddNoteScreen

            AddEditNotesScreen() {
                navController.popBackStack()
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    AddEditNotesScreen() {
    }
//    CustomWearApp()
}

