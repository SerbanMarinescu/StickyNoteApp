package com.example.noteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.feature_note.presentation.ui.theme.Blue
import com.example.noteapp.feature_note.presentation.ui.theme.Green
import com.example.noteapp.feature_note.presentation.ui.theme.Orange
import com.example.noteapp.feature_note.presentation.ui.theme.RedOrange
import com.example.noteapp.feature_note.presentation.ui.theme.RedPink
import com.example.noteapp.feature_note.presentation.ui.theme.Yellow

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
){
    companion object{
        val noteColors = listOf(Orange, Yellow, Blue, RedPink, Green, RedOrange)
    }
}

class InvalidNoteException(message: String) : Exception(message)
