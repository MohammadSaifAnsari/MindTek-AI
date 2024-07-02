package com.mohdsaifansari.mindtek.AITool

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mohdsaifansari.mindtek.AITool.Data.ToolItem
import com.mohdsaifansari.mindtek.AITool.Modal.AIToolViewModal
import com.mohdsaifansari.mindtek.Data.Date
import com.mohdsaifansari.mindtek.R
import com.mohdsaifansari.mindtek.Setting.ThemePreference
import com.mohdsaifansari.mindtek.Setting.dataStore
import com.mohdsaifansari.mindtek.ui.theme.MindtekTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class SummarizerActivity : ComponentActivity() {

    private val uriState = MutableStateFlow("")

    private val launcher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            uriState.update {
                uri.toString()
            }
        }

    }
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val context = this@SummarizerActivity


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color(220, 226, 241, 255).toArgb(),
                Color.Transparent.toArgb()
            ), navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        setContent {
            val themePreference = remember { ThemePreference(context.dataStore) }
            var isDarkTheme by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                themePreference.readThemePreferences().collect {
                    isDarkTheme = it
                }
            }
            MindtekTheme(darkTheme = isDarkTheme) {
                val tool_title = intent.getStringExtra("TOOL_TITLE")
                val tool_subtitle = intent.getStringExtra("TOOL_SUBTITLE")
                if ((tool_title == ToolItem.TextSummarizer.title) || (tool_title == ToolItem.StorySummarizer.title) || (tool_title == ToolItem.ParagraphSummarizer.title)) {
                    SummarizerScreen(
                        title = tool_title.toString(),
                        subtitle = tool_subtitle.toString()
                    )
                } else {
                    Generation(
                        tool_title.toString(),
                        tool_subtitle.toString(),
                        this@SummarizerActivity
                    )
                }


            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SummarizerScreen(title: String, subtitle: String) {
        var path = ""
        var inputText = ""
        var outputMessage = ""
        val viewmodel: AIToolViewModal = viewModel()
        var text by remember {
            mutableStateOf("")
        }
        val extractedText = remember {
            mutableStateOf("")
        }
        var uri = uriState.collectAsState().value

        val bottomSheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember {
            mutableStateOf(false)
        }
        val clipboardManager = LocalClipboardManager.current

        var isEnabledButton by remember {
            mutableStateOf(false)
        }
        Scaffold(
            topBar = {
                SummarizerHeader(title = title, this@SummarizerActivity)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary, // #dee4f4
                                MaterialTheme.colorScheme.background
                            ), start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            ) {
                Text(
                    text = subtitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 20.dp),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(start = 20.dp, end = 20.dp)
                )
                {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
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
                    if (text.isEmpty() && extractedText.value.isEmpty()) {
                        path = uri

                        if (path.isNotEmpty()) {
                            Log.d("saif123", path)
                            extractData(extractedText, path, this@SummarizerActivity)
                            uri = ""
                            path = ""
                        }
                        if (extractedText.value.isNotEmpty()) {
                            text = extractedText.value.trim()
                            extractedText.value = ""
                        }
                        FloatingActionButton(
                            onClick = {
                                launcher.launch("application/pdf")
                            }, modifier = Modifier
                                .align(Alignment.Center)
                                .size(100.dp),
                            containerColor = Color(160, 166, 181, 255),
                            shape = CircleShape
                        ) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(MaterialTheme.colorScheme.background),
                                tint = Color(160, 166, 181, 255)
                            )
                        }
                    }
                }

                if (text.isNotEmpty()) {
                    isEnabledButton = true
                }
                Button(
                    onClick = {
                        inputText = text
                        text = ""
                        extractedText.value = ""
                        val uid = firebaseAuth.currentUser?.uid.toString()
                        firestore.collection("History").document(uid)
                            .update("Count", FieldValue.increment(1))
                            .addOnSuccessListener {
                            }.addOnFailureListener {
                                Toast.makeText(
                                    this@SummarizerActivity,
                                    "Something went wrong",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        viewmodel.sendMessage(viewmodel.PromptCase(title = title) + inputText)
                        showBottomSheet = true
                    },
                    enabled = isEnabledButton,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(start = 20.dp, top = 4.dp, end = 20.dp, bottom = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = "Summarize",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
                                    Toast.makeText(
                                        this@SummarizerActivity,
                                        "Copied ",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
            if ((isEnabledButton == false) && inputText.isNotEmpty() && outputMessage.isNotEmpty()) {
                viewmodel.savedData(inputText, outputMessage, title, this@SummarizerActivity)
            }
            viewmodel.list.clear()
            isEnabledButton = false
        }

    }
}





