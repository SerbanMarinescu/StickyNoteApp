package com.example.noteapp.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.noteapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM Note")
    fun getNotes(): Flow<List<Note>>
}