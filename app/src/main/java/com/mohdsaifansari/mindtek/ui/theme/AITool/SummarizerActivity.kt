package com.mohdsaifansari.mindtek.ui.theme.AITool

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohdsaifansari.mindtek.ui.theme.AITool.Data.ToolItem
import com.mohdsaifansari.mindtek.ui.theme.AITool.Modal.AIToolViewModal
import com.mohdsaifansari.mindtek.ui.theme.AITool.ui.theme.MindtekTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindtekTheme {
                val tool_title = intent.getStringExtra("TOOL_TITLE")
                val tool_subtitle = intent.getStringExtra("TOOL_SUBTITLE")
                if ((tool_title == ToolItem.TextSummarizer.title)||(tool_title == ToolItem.StorySummarizer.title)||(tool_title == ToolItem.ParagraphSummarizer.title)){
                    SummarizerScreen(title = tool_title.toString(), subtitle = tool_subtitle.toString())
                }else{
                    Generation(tool_title.toString(), tool_subtitle.toString(),this@SummarizerActivity)
                }



            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SummarizerScreen(title: String,subtitle:String){
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

        Scaffold(
            topBar = {
                SummarizerHeader(title = title)
            }
        ) {innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(
                    text = subtitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 20.dp),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )
                if(text.isEmpty() && extractedText.value.isEmpty()){
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp),
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                        },
                        placeholder = {
                            Text(text = "Type a prompt")
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(start = 20.dp, end = 20.dp))
                    {
                        path = uri

                        if(path.isNotEmpty()){
                            Log.d("saif123",path)
                            extractData(extractedText,path,this@SummarizerActivity)
                            uri = ""
                            path = ""
                        }
                        if (extractedText.value.isNotEmpty()){
                            text = extractedText.value
                            extractedText.value = ""
                        }
                        IconButton(
                            onClick = {
                                launcher.launch("application/pdf")
                            }, modifier = Modifier
                                .padding(4.dp)
                                .align(Alignment.Center)
                                .size(100.dp)
                        ) {
                            Icon(
                                Icons.Default.AddCircle, contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(Color.Transparent), tint = Color.Black
                            )
                        }

                    }
                }else{
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(start = 20.dp, end = 20.dp))
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
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                }
                Button(
                    onClick = {
                        inputText = text
                        text = ""
                        extractedText.value = ""
                        viewmodel.sendMessage(PromptCase(title = title) + inputText)
                        showBottomSheet = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(start = 20.dp, top = 4.dp, end = 20.dp, bottom = 4.dp)
                ) {
                    Text(text = "Summarize", fontSize = 20.sp)
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
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
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
                                    Toast.makeText(this@SummarizerActivity,"Copied ", Toast.LENGTH_SHORT).show()
                                }, modifier = Modifier
                                    .padding(4.dp)
                                    .align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    Icons.Default.Share, contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color.Transparent), tint = Color.Black
                                )
                            }
                        }
                        SheetContentScreen(outputMessage)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
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
            viewmodel.list.clear()

        }

    }

}


