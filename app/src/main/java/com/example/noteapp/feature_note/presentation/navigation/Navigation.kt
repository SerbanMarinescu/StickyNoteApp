package com.example.noteapp.feature_note.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.noteapp.feature_note.presentation.note_details_screen.NoteDetailsScreen
import com.example.noteapp.feature_note.presentation.notes_screen.NotesScreen
import com.example.noteapp.feature_note.presentation.util.Screen

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.NotesScreen.route){
        composable(Screen.NotesScreen.route){
            NotesScreen(navController = navController)
        }
        composable(
            route = Screen.NoteDetailsScreen.route + "?noteId={noteId}&noteColor={noteColor}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(name = "noteColor"){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            NoteDetailsScreen(
                navController = navController,
                noteColor = it.arguments?.getInt("noteColor") ?: -1
            )
        }
    }
}