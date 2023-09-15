package com.example.noteapp.feature_note.presentation.util

sealed class Screen(val route: String){
    object NotesScreen: Screen("notes_screen")
    object NoteDetailsScreen: Screen("note_details_screen")
}
