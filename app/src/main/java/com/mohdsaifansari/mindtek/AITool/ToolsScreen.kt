package com.mohdsaifansari.mindtek.AITool

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.mohdsaifansari.mindtek.AITool.Modal.AIToolViewModal
import com.mohdsaifansari.mindtek.Components.LoadingAnimation
import com.mohdsaifansari.mindtek.MainActivity


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
            ToolsHeader(title, context)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary, // #dee4f4
                            MaterialTheme.colorScheme.background
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
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.onBackground
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
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                    cursorColor = MaterialTheme.colorScheme.onBackground
                )
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
                    viewmodel.sendMessage(viewmodel.PromptCase(title = title) + inputText)
                    showBottomSheet = true
                }, enabled = isEnabledButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(text = "Generate", color = MaterialTheme.colorScheme.onBackground)
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
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(MaterialTheme.colorScheme.primary),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                showBottomSheet = false
                            }, modifier = Modifier
                                .padding(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Transparent),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(outputMessage))
                                Toast.makeText(context, "Copied ", Toast.LENGTH_SHORT).show()
                            }, modifier = Modifier
                                .padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.copytext),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Transparent),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    SheetContentScreen(
                        outputMessage.substring(1, (outputMessage.length - 1)),
                        viewmodel
                    )
                    showDialog = false
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        IconButton(
                            onClick = {
                                viewmodel.list.clear()
                            }, modifier = Modifier
                                .padding(4.dp)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Transparent),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
        if ((isEnabledButton == false) && inputText.isNotEmpty() && outputMessage.isNotEmpty() && (showBottomSheet == false)) {
            Log.d("tool1234", inputText + outputMessage + title)
            viewmodel.savedData(inputText, outputMessage, title, context)
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
fun SheetContentScreen(getResponse: String, viewModal: AIToolViewModal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .verticalScroll(rememberScrollState()),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            text = getResponse, textAlign = TextAlign.Justify, fontSize = 14.sp,
            modifier = Modifier
                .padding(16.dp)
        )
        if (viewModal.isloadingAnimation.collectAsState().value) {
            LoadingAnimation(circleSize = 10.dp, spaceBetween = 4.dp, travelDistance = 10.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsHeader(title: String, context: Context) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title, modifier = Modifier.padding(5.dp),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = Color.White
        ), navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        context.startActivity(intent)
                    },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}

