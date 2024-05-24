package com.example.noteswearosapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material3.IconButton
import com.example.noteswearosapp.models.Note
import com.example.noteswearosapp.presentation.theme.LightOrange
import com.example.noteswearosapp.presentation.theme.Orange


@Composable
fun NotesListScreen(onNoteClick: (Note) -> Unit, onAddNoteClick: () -> Unit){
//    val mainViewModel: MainViewModel = hiltViewModel()
//    val response = mainViewModel.notesStateFlow.collectAsState().value
//    LaunchedEffect(Unit){
//        mainViewModel.getNotes()
//    }

    Box (modifier = Modifier.padding(top = 10.dp)){
        val notesList = mutableListOf<Note>(
            Note(
                title = "Title 1",
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut nisl nec purus lacinia tincidunt. Nullam nec nunc nec purus lacinia tincidunt. Nullam nec nunc"
            ),
            Note(
                title = "Title 2",
                content = "Content 2"
            ),
        )
        NotesList(notesList){
            onNoteClick(it)
        }
        AddIconButton(){
            onAddNoteClick()
        }
    }
}


@Composable
fun NotesList(notesList: List<Note> = emptyList(), onNoteClick: (Note) -> Unit) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize(), ){
        items(items = notesList){ note ->
            NotesItem(note,onNoteClick)
        }
    }

}


@Composable
fun NotesItem(note: Note,onNoteClick: (Note) -> Unit) {
    TitleCard(
        onClick = { onNoteClick(note) },
        title = { Text(note.title)},
        content = { Text(text=note.content, maxLines = 2, overflow = TextOverflow.Ellipsis)},
        backgroundPainter = CardDefaults.cardBackgroundPainter(LightOrange, LightOrange),
        titleColor = Color.Black,
        contentColor = Color.Black
    )
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
//            .shadow(4.dp, clip = true)
//            .background(LightOrange)
//            .padding(16.dp)
//            .padding(start = 8.dp)
//        ,
//
//        ) {
//
//        Text(
//            modifier = Modifier.padding(bottom = 8.dp),
//            text = note.title,
//            color = Color.Black,
//            style = MaterialTheme.typography.caption1,
//            maxLines = 1
//        )
//        Text(
//            modifier = Modifier.padding(bottom = 8.dp),
//            text = note.content,
//            color = Color.Black,
//            style = MaterialTheme.typography.body1,
//            maxLines = 3,
//            overflow = TextOverflow.Ellipsis)
//
//    }
}

@Composable
fun BoxScope.AddIconButton(onClick: () -> Unit) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(15))
            .background(Orange)
    ) {

        Icon(
            modifier = Modifier.
            size(20.dp).align(Alignment.Center),
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            tint = Color.White
        )
    }
}