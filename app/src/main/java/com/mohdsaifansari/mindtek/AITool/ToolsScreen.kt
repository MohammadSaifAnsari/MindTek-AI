package com.mohdsaifansari.mindtek.AITool

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mohdsaifansari.mindtek.R
import com.mohdsaifansari.mindtek.AITool.Data.ToolItem
import com.mohdsaifansari.mindtek.AITool.Modal.AIToolViewModal


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Generation(title: String, subTitle: String, context: Context) {
    val firestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    var inputText = ""
    var outputMessage = ""
    val viewmodel: AIToolViewModal = viewModel()
    var text by remember {
        mutableStateOf("")
    }
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val clipboardManager = LocalClipboardManager.current
    var showDialog by remember {
        mutableStateOf(false)
    }
    var isEnabledButton by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            ToolsHeader(title)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFDCE2F1), // #dee4f4
                            Color(0xFFFFFFFF)
                        ), start = Offset(0f, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
                .fillMaxSize()
        ) {
            Text(
                text = subTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 20.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(250.dp),
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                placeholder = {
                    Text(text = "Type a prompt")
                },
                shape = RoundedCornerShape(16.dp)
            )
            if (text.isNotEmpty()) {
                isEnabledButton = true
            }
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp, top = 20.dp),
                onClick = {
                    inputText = text
                    text = ""
                    showDialog = true
                    val uid = firebaseAuth.currentUser?.uid.toString()
                    firestore.collection("History").document(uid)
                        .update("Count", FieldValue.increment(1))
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show();
                        }
                    viewmodel.sendMessage(PromptCase(title = title) + inputText)
                    showBottomSheet = true
                }, enabled = isEnabledButton
            ) {
                Text(text = "Generate")
            }

        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .windowInsetsPadding(WindowInsets.systemBars),
                dragHandle = null, tonalElevation = 100.dp
            ) {
                outputMessage = viewmodel.list.toList().toString()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(220, 226, 241, 255))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color(220, 226, 241, 255))
                    ) {
                        IconButton(
                            onClick = {
                                showBottomSheet = false
                            }, modifier = Modifier
                                .padding(4.dp)
                                .align(Alignment.CenterStart)
                        ) {
                            Icon(
                                Icons.Default.Close, contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Transparent), tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(outputMessage))
                                Toast.makeText(context, "Copied ", Toast.LENGTH_SHORT).show()
                            }, modifier = Modifier
                                .padding(4.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.copytext),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Transparent),
                                tint = Color.Black
                            )
                        }
                    }
                    SheetContentScreen(outputMessage.substring(1, (outputMessage.length - 1)))
                    showDialog = false
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color(220, 226, 241, 255))
                    ) {
                        IconButton(
                            onClick = {
                                viewmodel.list.clear()
                            }, modifier = Modifier
                                .padding(4.dp)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                Icons.Default.Delete, contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Transparent), tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
        if ((isEnabledButton == false) && inputText.isNotEmpty() && outputMessage.isNotEmpty() && (showBottomSheet == false)) {
            Log.d("tool1234", inputText + outputMessage + title)
            savedData(inputText, outputMessage, title, context)
        }
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                CircularProgressIndicator(color = Color.Red)
            }
        }
        viewmodel.list.clear()
        isEnabledButton = false
    }
}


@Composable
fun SheetContentScreen(getResponse: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardColors(
            containerColor = Color.White, contentColor = Color.Black,
            disabledContainerColor = Color.White, disabledContentColor = Color.Black
        )
    ) {
        Text(
            text = getResponse, textAlign = TextAlign.Justify, fontSize = 14.sp,
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsHeader(title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title, modifier = Modifier.padding(5.dp),
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Serif
            )
        },
        colors = TopAppBarColors(
            containerColor = Color(220, 226, 241, 255),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            scrolledContainerColor = Color.White
        ), navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                modifier = Modifier.padding(5.dp),
                contentDescription = null
            )
        }
    )
}

fun PromptCase(title: String): String {
    if (title == ToolItem.MailGeneration.title) {
        return ToolItem.MailGeneration.toolPrompt
    } else if (title == ToolItem.BlogGeneration.title) {
        return ToolItem.BlogGeneration.toolPrompt
    } else if (title == ToolItem.BlogSection.title) {
        return ToolItem.BlogSection.toolPrompt
    } else if (title == ToolItem.BlogIdeas.title) {
        return ToolItem.BlogIdeas.toolPrompt
    } else if (title == ToolItem.ParagraphGenerator.title) {
        return ToolItem.ParagraphGenerator.toolPrompt
    } else if (title == ToolItem.GenerateArticle.title) {
        return ToolItem.GenerateArticle.toolPrompt
    } else if (title == ToolItem.CreativeStory.title) {
        return ToolItem.CreativeStory.toolPrompt
    } else if (title == ToolItem.CreativeLetter.title) {
        return ToolItem.CreativeLetter.toolPrompt
    } else if (title == ToolItem.LoveLetter.title) {
        return ToolItem.LoveLetter.toolPrompt
    } else if (title == ToolItem.Poems.title) {
        return ToolItem.Poems.toolPrompt
    } else if (title == ToolItem.SongLyrics.title) {
        return ToolItem.SongLyrics.toolPrompt
    } else if (title == ToolItem.FoodRecipe.title) {
        return ToolItem.FoodRecipe.toolPrompt
    } else if (title == ToolItem.GrammerCorrection.title) {
        return ToolItem.GrammerCorrection.toolPrompt
    } else if (title == ToolItem.AnswerQuestion.title) {
        return ToolItem.AnswerQuestion.toolPrompt
    } else if (title == ToolItem.ActivePassive.title) {
        return ToolItem.ActivePassive.toolPrompt
    } else if (title == ToolItem.PassiveActive.title) {
        return ToolItem.PassiveActive.toolPrompt
    } else if (title == ToolItem.JobDescription.title) {
        return ToolItem.JobDescription.toolPrompt
    } else if (title == ToolItem.Resume.title) {
        return ToolItem.Resume.toolPrompt
    } else if (title == ToolItem.InterviewQuestions.title) {
        return ToolItem.InterviewQuestions.toolPrompt
    } else if (title == ToolItem.TextSummarizer.title) {
        return ToolItem.TextSummarizer.toolPrompt
    } else if (title == ToolItem.StorySummarizer.title) {
        return ToolItem.StorySummarizer.toolPrompt
    } else {
        return ToolItem.ParagraphSummarizer.toolPrompt
    }
}