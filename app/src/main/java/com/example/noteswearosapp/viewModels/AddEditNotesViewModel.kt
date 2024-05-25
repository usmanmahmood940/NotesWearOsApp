package com.example.noteswearosapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.noteswearosapp.models.Note
import com.example.noteswearosapp.repositories.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(private val repository: NotesRepository): ViewModel(){

    var noteTitle = mutableStateOf("No Title")
    var noteContent = mutableStateOf("")
    var noteId:String = ""


    fun upsertNote(note: Note) {
        repository.upsertNote(note)
    }

    suspend fun deleteNote(noteId: String) {
        repository.deleteNotes(noteId)
    }

     suspend fun getNoteById(noteId: String) {
         this.noteId = noteId
         repository.getNoteById(noteId)?.apply {
                noteTitle.value = title
                noteContent.value = content
         }
    }

}