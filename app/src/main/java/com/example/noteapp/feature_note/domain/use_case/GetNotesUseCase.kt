package com.example.noteapp.feature_note.domain.use_case

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepository
import com.example.noteapp.feature_note.domain.util.NoteOrder
import com.example.noteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.DESCENDING)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->

            when(noteOrder){
                is NoteOrder.Color -> {
                    when(noteOrder.orderType){
                        OrderType.ASCENDING -> notes.sortedBy { it.color }
                        OrderType.DESCENDING -> notes.sortedByDescending { it.color }
                    }
                }
                is NoteOrder.Date -> {
                    when(noteOrder.orderType){
                        OrderType.ASCENDING -> notes.sortedBy { it.timestamp }
                        OrderType.DESCENDING -> notes.sortedByDescending { it.timestamp }
                    }
                }
                is NoteOrder.Title -> {
                    when(noteOrder.orderType){
                        OrderType.ASCENDING -> notes.sortedBy { it.title.lowercase() }
                        OrderType.DESCENDING -> notes.sortedByDescending { it.title.lowercase() }
                    }
                }
            }
        }
    }
}