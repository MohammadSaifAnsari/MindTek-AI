package com.mohdsaifansari.mindtek.AITool

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mohdsaifansari.mindtek.AITool.Modal.AIToolViewModal
import com.mohdsaifansari.mindtek.AdsMob.CoinViewModal
import com.mohdsaifansari.mindtek.Components.HeaderComponent
import com.mohdsaifansari.mindtek.Components.NavigationItem
import com.mohdsaifansari.mindtek.LocalModelPreference
import com.mohdsaifansari.mindtek.Network.NetworkConnectivityChecker
import com.mohdsaifansari.mindtek.R
import com.mohdsaifansari.mindtek.SentencePieceTokenizer
import com.mohdsaifansari.mindtek.Setting.ThemeChange.ThemePreference
import com.mohdsaifansari.mindtek.Setting.ThemeChange.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainSummarizerScreen(
    navController: NavController,
    context: Context,
    pdfUri: MutableStateFlow<String>,
    launcher: ActivityResultLauncher<String>,
    clearPdfUri: (String) -> Unit,
    spModelPath: String,
    assets: AssetManager
) {
    val titleFromNav = navController.currentBackStackEntry?.arguments?.getString("title")
    val subtitleFromNav = navController.currentBackStackEntry?.arguments?.getString("subtitle")
    SummarizerScreen(
        title = titleFromNav.toString(),
        subtitle = subtitleFromNav.toString(),
        context = context,
        pdfUri = pdfUri,
        launcher = launcher,
        navController = navController,
        clearUri = {
            clearPdfUri(it)
        },
        spModelPath,
        assets
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizerScreen(
    title: String,
    subtitle: String,
    context: Context,
    pdfUri: MutableStateFlow<String>,
    launcher: ActivityResultLauncher<String>,
    navController: NavController,
    clearUri: (String) -> Unit,
    spModelPath: String,
    assets: AssetManager
) {
    val firestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    var path = ""
    var inputText by remember {
        mutableStateOf("")
    }
    var outputMessage = ""
    val viewmodel: AIToolViewModal = viewModel()
    var text by remember {
        mutableStateOf("")
    }
    var uri = pdfUri.collectAsState().value

    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val clipboardManager = LocalClipboardManager.current

    var isEnabledButton by remember {
        mutableStateOf(false)
    }

    val coinViewModal = viewModel<CoinViewModal>()


    val viewmodelSum: SummarizerViewModel = viewModel()

    var isLocalSumModel by remember { mutableStateOf(false) }
    val modelChange = remember { LocalModelPreference(context.dataStore) }
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var localSumModelResult by remember { mutableStateOf("") }

    var tokenCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        modelChange.readModelPreferences().collect {
            isLocalSumModel = it
        }
    }
    Scaffold(
        topBar = {
            HeaderComponent(title = title, navigationIconClickable = {
                navController.navigate(NavigationItem.HomeScreen.route) {
                    popUpTo(NavigationItem.SummarizerNav.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
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
            BackHandler {
                clearUri("")
                navController.navigate(NavigationItem.HomeScreen.route) {
                    popUpTo(NavigationItem.SummarizerNav.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
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
            Column(modifier = Modifier
                .fillMaxWidth().height(100.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier
                    .fillMaxWidth()) {
                    Text(text = "Work Offline",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp).weight(0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Switch(
                        checked = isLocalSumModel,
                        onCheckedChange = { isChecked ->
                            isLocalSumModel = isChecked
                            coroutineScope.launch { modelChange.saveModelPreferences(isChecked) }
                        },
                        thumbContent = {
                            Icon(
                                imageVector = if (isLocalSumModel) {
                                    Icons.Default.Check
                                } else {
                                    Icons.Default.Clear
                                }, contentDescription = null
                            )
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onSurface,
                            checkedTrackColor = MaterialTheme.colorScheme.secondary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                            uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                            uncheckedBorderColor = MaterialTheme.colorScheme.secondary,
                            checkedIconColor = MaterialTheme.colorScheme.onBackground,
                            uncheckedIconColor = MaterialTheme.colorScheme.onBackground
                        ), modifier = Modifier.padding(end = 10.dp)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth().weight(1f)) {
                    Text(text = "Word Count",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 10.dp, top = 5.dp, bottom = 5.dp).weight(0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(text = "$tokenCount",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp, end = 20.dp, top = 5.dp, bottom = 5.dp).weight(0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

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
                        val tokens = SentencePieceTokenizer.encode(newText)
                        if(isLocalSumModel){
                            try {
                                if (tokens.size <= 1500) {
                                    try {
                                        if (tokens.size >= 100) {
                                            isEnabledButton = true
                                        }else{
                                            isEnabledButton = false
                                        }
                                        tokenCount = tokens.size
                                    } catch (e: Exception) {
                                        isEnabledButton = false
                                        tokenCount = 0
                                    }
                                } else {
                                    Toast.makeText(context,"Max 1500 Token Limit Reached",Toast.LENGTH_SHORT).show()
                                    tokenCount = tokens.size
                                    isEnabledButton = false
                                    // Optionally show a message or simply ignore the input that would exceed the limit.
                                    Log.d("Summarizer", "Input exceeds 512 tokens; change ignored")
                                }
                                text = newText
                            } catch (e: Exception) {
                                Log.e("Summarizer", "Tokenization error: ${e.message}")
                                text = newText  // Fallback: allow input if tokenization fails
                            }
                        }else{
                            tokenCount = tokens.size
                            text = newText
                        }
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
                if (text.isEmpty()) {
                    path = uri

                    if (path.isNotEmpty()) {
                        runBlocking {
                            viewmodel.splitPdfIntoImages(getText = { text = it }, path, context)
                        }
                        uri = ""
                        path = ""
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

                    if(isLocalSumModel){
                        coroutineScope.launch{
                            isEnabledButton = false
                            isLoading = true
                            val result = withContext(Dispatchers.IO){
                                viewmodelSum.runSummarizationWithBatchedBeam(spModelPath,text,assets)
                            }
                            localSumModelResult = result
                            isLoading = false
                            showBottomSheet = true
                        }
                    }else{
                        if (coinViewModal.isCoin.value >= 10) {
                            inputText = text
                            text = ""
                            clearUri("")
                            val uid = firebaseAuth.currentUser?.uid.toString()
                            firestore.collection("History").document(uid)
                                .update("Count", FieldValue.increment(1))
                                .addOnSuccessListener {
                                    coinViewModal.subtractCoin(context)
                                    coinViewModal.getCoin()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }
                            viewmodel.sendMessage(viewmodel.PromptCase(title = title) + inputText)
                            showBottomSheet = true
                        } else {
                            Toast.makeText(
                                context,
                                "Insufficient coins",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                enabled = isEnabledButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(start = 20.dp, top = 4.dp, end = 20.dp, bottom = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(2.dp).align(Alignment.CenterVertically), color = Color.White )
                } else {
                    Text(
                        text = "Summarize",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
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
                if (isLocalSumModel){
                    outputMessage = viewmodelSum.isModelResult.collectAsState().value

                }else{
                    outputMessage = viewmodel.list.toList().toString()
                }
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
                                    context,
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
                        viewmodel,
                        isLocalSumModel,
                        viewmodelSum
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
        Log.d("sum1234", inputText + outputMessage + isEnabledButton.toString())
        if (!isEnabledButton && inputText.isNotEmpty() && outputMessage.isNotEmpty()) {
            viewmodel.savedData(inputText, outputMessage, title, context)
        }
        viewmodel.list.clear()
        isEnabledButton = false
    }

}


