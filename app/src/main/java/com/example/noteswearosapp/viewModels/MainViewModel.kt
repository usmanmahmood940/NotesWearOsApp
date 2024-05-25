package com.example.noteswearosapp.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteswearosapp.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.noteswearosapp.repositories.NotesRepository

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NotesRepository ):ViewModel() {
    val notesStateFlow = repository.notesStateFlow
    val notes = mutableStateListOf<Note>()

     fun getNotes() {
         repository.getNotes()
    }

    fun upsertNote(note: Note) {
        repository.upsertNote(note)
    }
    suspend fun deleteNotes(noteId: String) {
        repository.deleteNotes(noteId)
    }


}