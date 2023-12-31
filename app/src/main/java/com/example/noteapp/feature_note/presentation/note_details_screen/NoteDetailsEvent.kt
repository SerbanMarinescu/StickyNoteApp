package com.example.noteapp.feature_note.presentation.note_details_screen

import androidx.compose.ui.focus.FocusState

sealed class NoteDetailsEvent{
    data class EnteredTitle(val value: String): NoteDetailsEvent()
    data class EnteredContent(val value: String): NoteDetailsEvent()
    data class ChangeTitleFocus(val focusState: FocusState): NoteDetailsEvent()
    data class ChangeContentFocus(val focusState: FocusState): NoteDetailsEvent()
    data class ChangeColor(val color: Int): NoteDetailsEvent()
    object SaveNote: NoteDetailsEvent()
}
