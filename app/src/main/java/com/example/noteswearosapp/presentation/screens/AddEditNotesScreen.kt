package com.example.noteswearosapp.presentation.screens

import android.Manifest
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
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.noteswearosapp.presentation.MainActivity
import com.example.noteswearosapp.presentation.theme.LightOrange
import com.example.noteswearosapp.viewModels.AddEditNotesViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddEditNotesScreen(isEdit: Boolean=false,noteId: String? = null ,onFinish:()->Unit) {
    val addEditNotesViewModel: AddEditNotesViewModel = hiltViewModel()
    var isLoaded by remember { mutableStateOf(true) }

    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val context = LocalContext.current
    val mainActivity = context as MainActivity
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

    if(isEdit){
        LaunchedEffect(Unit){
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
                    .weight(3f), addEditNotesViewModel.noteTitle,addEditNotesViewModel.focusedTitle
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
                    mainActivity.setupSpeechRecognizer()
                    mainActivity.startListening()
//                    CoroutineScope(Dispatchers.IO).launch {
//                        addEditNotesViewModel.apply {
//                            upsertNote(
//                                Note(
//                                    id = noteId?:generateRandomStringWithTime(),
//                                    title = noteTitle.value,
//                                    content = noteContent.value
//                                )
//                            )
//                            withContext(Dispatchers.Main) {
//                                onFinish()
//                            }
//                        }
//                    }
                }
            )

        }
    }
    else{
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Loading...", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun Title(
    modifier: Modifier = Modifier,
    noteTitle: MutableState<String>,
    focusedTitle: MutableState<Boolean>
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .onFocusChanged { focusState ->
                focusedTitle.value = focusState.isFocused
            }
        ,
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
fun Content(modifier: Modifier = Modifier, noteContent: MutableState<String>, focusedContent: MutableState<Boolean>
) {

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
    isEdit: Boolean = false, onDelete: () -> Unit, onSave: () -> Unit
) {
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
fun RowScope.ActionButton(onClick: () -> Unit, contentDescription: String, icon: ImageVector) {
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
                tint = Color.Black,
                modifier = Modifier.size(50.dp)

            )
        }
    }

}
