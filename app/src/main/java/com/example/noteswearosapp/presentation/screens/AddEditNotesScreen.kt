package com.example.noteswearosapp.presentation.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.noteswearosapp.R
import com.example.noteswearosapp.Utils.HelperClass.generateRandomStringWithTime
import com.example.noteswearosapp.models.Note
import com.example.noteswearosapp.presentation.theme.LightOrange
import com.example.noteswearosapp.viewModels.AddEditNotesViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddEditNotesScreen(isEdit: Boolean = false, noteId: String? = null, onFinish: () -> Unit) {
    val addEditNotesViewModel: AddEditNotesViewModel = hiltViewModel()
    var isLoaded by remember { mutableStateOf(true) }



    var spokenText by remember { mutableStateOf<String?>(null) }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            spokenText = results?.firstOrNull()
            spokenText?.let {
                addEditNotesViewModel.apply {
                    if (focusedContent.value) {
                        noteContent.value = noteContent.value + " " + it
                    }
                    else if (focusedTitle.value) {
                        noteTitle.value = it
                    }
                    else{
                        noteContent.value = noteContent.value + " " + it
                    }

                }
            }
        }
    }
    CheckPermission()
    if (isEdit) {
        LaunchedEffect(Unit) {
            isLoaded = false
            withContext(Dispatchers.IO) {
                noteId?.let {
                    addEditNotesViewModel.getNoteById(it)
                }
            }
            isLoaded = true
        }
    }
    if (isLoaded) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Title(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(4f), addEditNotesViewModel.noteTitle
            )
            Content(Modifier.weight(6f), addEditNotesViewModel.noteContent)
            BottomButtonsBar(
                isEdit = isEdit,
                onDelete = {
                    CoroutineScope(Dispatchers.IO).launch {
                        noteId?.let {
                            addEditNotesViewModel.deleteNote(it)
                        }
                        withContext(Dispatchers.Main) {
                            onFinish()
                        }
                    }

                },
                onSave = {
                    CoroutineScope(Dispatchers.IO).launch {
                        addEditNotesViewModel.apply {
                            upsertNote(
                                Note(
                                    id = noteId ?: generateRandomStringWithTime(),
                                    title = noteTitle.value,
                                    content = noteContent.value
                                )
                            )
                            withContext(Dispatchers.Main) {
                                onFinish()
                            }
                        }
                    }
                },
                speechRecognizerLauncher = speechRecognizerLauncher
            )

        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Loading...", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun CheckPermission() {
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) {
            // Show a rationale if needed (optional)
        } else {
            // Request the permission
            SideEffect {
                permissionState.launchPermissionRequest()

            }

        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier, noteTitle: MutableState<String>) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White

        ),
        maxLines = 1,
        value = noteTitle.value,
        placeholder = {
            Text(
                "Enter Title", Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        },
        onValueChange = { noteTitle.value = it },
        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp, textAlign = TextAlign.Center),

        )
}

@Composable
fun Content(modifier: Modifier = Modifier, noteContent: MutableState<String>) {

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        value = noteContent.value,
        onValueChange = { noteContent.value = it },
        placeholder = { Text("Enter Content") },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),


        )
}


@Composable
fun ColumnScope.BottomButtonsBar(
    isEdit: Boolean = false,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    speechRecognizerLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val addEditNotesViewModel: AddEditNotesViewModel = hiltViewModel()
    val micState by remember {
        addEditNotesViewModel.micState
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.5f)
            .background(LightOrange),
        horizontalArrangement = Arrangement.End
    ) {
        ActionButton(
            onClick = { startListening(speechRecognizerLauncher)},
            contentDescription = "Voice Type",
            icon = ImageVector.vectorResource(id = R.drawable.ic_mic),
            iconColor = if (micState) Color.Green else Color.Black
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.05f), color = Color.Black
    )

    if (isEdit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(LightOrange),
            horizontalArrangement = Arrangement.End
        ) {

            ActionButton(
                onClick = {
                    onDelete()
                }, contentDescription = "Delete Note", icon = Icons.Default.Delete
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.05f), color = Color.Black
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
            .background(LightOrange),
        horizontalArrangement = Arrangement.End
    ) {

        ActionButton(
            onClick = {
                onSave()
            }, contentDescription = "Save Note", icon = Icons.Default.Done
        )
    }
}


@Composable
fun RowScope.ActionButton(onClick: () -> Unit, contentDescription: String, icon: ImageVector,iconColor:Color = Color.Black) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(2f)
            .clickable(onClick = onClick)
    ) {
        IconButton(
            onClick = { onClick() }, modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(50.dp)

            )
        }
    }

}


fun startListening(speechRecognizerLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
    }
    speechRecognizerLauncher.launch(intent)
}

