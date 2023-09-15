package com.example.noteapp.feature_note.presentation.note_details_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent =
        mutableStateOf(NoteTextFieldState(hint = "Enter content for the note..."))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var curNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let {
            if(it != -1){
                viewModelScope.launch {
                    noteUseCases.getNoteUseCase(it)?.also {note ->
                        curNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            title = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            title = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteDetailsEvent) {
        when (event) {
            is NoteDetailsEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is NoteDetailsEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.title.isBlank()
                )
            }

            is NoteDetailsEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.title.isBlank()
                )
            }

            is NoteDetailsEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    title = event.value
                )
            }

            is NoteDetailsEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    title = event.value
                )
            }

            is NoteDetailsEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                title = noteTitle.value.title,
                                content = noteContent.value.title,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = curNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.saveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(UiEvent.showSnackbar(
                            message = e.message ?: "Couldn't save note"
                        ))
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class showSnackbar(val message: String) : UiEvent()
        object saveNote : UiEvent()
    }
}