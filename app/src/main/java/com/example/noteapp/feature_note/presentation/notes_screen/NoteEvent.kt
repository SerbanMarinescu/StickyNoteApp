package com.example.noteapp.feature_note.presentation.notes_screen

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.util.NoteOrder

sealed class NoteEvent{
    object ToggleOrderSection: NoteEvent()
    data class OrderNotes(val noteOrder: NoteOrder): NoteEvent()
    //data class EditNote(val note: Note): NoteEvent()
    data class DeleteNote(val note: Note): NoteEvent()
    object RestoreNote: NoteEvent()
   // object AddNote: NoteEvent()
}
