package com.example.noteswearosapp.Utils

import com.example.noteswearosapp.models.Note


sealed class CustomResponse {
    data class Success(val notesList: List<Note>) : CustomResponse()
    data class Error(val message: String) : CustomResponse()
    object Loading : CustomResponse()
}