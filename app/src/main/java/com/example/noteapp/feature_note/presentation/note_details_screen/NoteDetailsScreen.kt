package com.example.noteapp.feature_note.presentation.note_details_screen

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.presentation.note_details_screen.components.TransparentTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: NoteDetailsViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val colorState = viewModel.noteColor.value

    val snackBarState = remember{
        SnackbarHostState()
    }

    val noteBackgroundAnimatable = remember {
        Animatable(Color(if(noteColor != -1) noteColor else colorState))
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {event ->
            when(event){
                is NoteDetailsViewModel.UiEvent.saveNote -> {
                    navController.navigateUp()
                }
                is NoteDetailsViewModel.UiEvent.showSnackbar -> {
                    snackBarState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                viewModel.onEvent(NoteDetailsEvent.SaveNote)
            },
                //modifier = Modifier.background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Note"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(noteBackgroundAnimatable.value)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Note.noteColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 3.dp,
                                    color = if (colorState == color.toArgb()) Color.Black else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch {
                                        noteBackgroundAnimatable.animateTo(
                                            targetValue = Color(color.toArgb()),
                                            animationSpec = tween(durationMillis = 500)
                                        )
                                    }
                                    viewModel.onEvent(NoteDetailsEvent.ChangeColor(color.toArgb()))
                                }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TransparentTextField(
                    text = titleState.title,
                    hint = titleState.hint,
                    onValueChange = {enteredValue ->
                        viewModel.onEvent(NoteDetailsEvent.EnteredTitle(enteredValue))
                    },
                    onFocusChange = {focusState ->
                        viewModel.onEvent(NoteDetailsEvent.ChangeTitleFocus(focusState))
                    },
                    isHintVisible = titleState.isHintVisible,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 20.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TransparentTextField(
                    text = contentState.title,
                    hint = contentState.hint,
                    onValueChange = {enteredValue ->
                        viewModel.onEvent(NoteDetailsEvent.EnteredContent(enteredValue))
                    },
                    onFocusChange = {focusState ->
                        viewModel.onEvent(NoteDetailsEvent.ChangeContentFocus(focusState))
                    },
                    isHintVisible = contentState.isHintVisible,
                    modifier = Modifier.fillMaxHeight(),
                    textStyle = TextStyle(fontSize = 15.sp)
                )
            }
        }
    }
}