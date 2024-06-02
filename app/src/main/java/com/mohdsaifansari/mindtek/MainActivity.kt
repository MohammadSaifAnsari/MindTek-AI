package com.mohdsaifansari.mindtek

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mohdsaifansari.mindtek.ui.theme.AITool.MainAiToolScreen
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.ChatHeader
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.ChatUiState
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.ChatViewModel
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.ModalChatBox
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.UserChatBox
import com.mohdsaifansari.mindtek.ui.theme.Components.BottomNavItem
import com.mohdsaifansari.mindtek.ui.theme.Components.LogInItem
import com.mohdsaifansari.mindtek.ui.theme.Components.MainBottomNavigation
import com.mohdsaifansari.mindtek.ui.theme.MindtekTheme
import com.mohdsaifansari.mindtek.ui.theme.ProfileScreen
import com.mohdsaifansari.mindtek.ui.theme.getUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val uriState = MutableStateFlow("")

    private val imagePicker = registerForActivityResult<PickVisualMediaRequest, Uri?>(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            uriState.update {
                uri.toString()
            }
        }

    }
    val context = this@MainActivity
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MindtekTheme {
                val navControllerSign = rememberNavController()
                NavHost(
                    navController = navControllerSign,
                    startDestination = if (firebaseAuth.currentUser != null) {
                        LogInItem.HomeScreen.route
                    } else {
                        LogInItem.AuthScreen.route
                    }
                ) {
                    navigation(
                        startDestination = LogInItem.SignIn.route,
                        route = LogInItem.AuthScreen.route
                    ) {
                        composable(LogInItem.SignIn.route) {
                            SignInScreen(auth = auth, context = context, navControllerSign)
                        }
                        composable(LogInItem.SignUp.route) {
                            SignUpScreen(auth = auth, context = context, navControllerSign)
                        }
                    }
                    navigation(
                        startDestination = LogInItem.MainScreen.route,
                        route = LogInItem.HomeScreen.route
                    ) {
                        composable(LogInItem.MainScreen.route) {
                            MainEntryPoint(context, navControllerSign)
                        }
                    }


                }
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainEntryPoint(context: Context, navControllerSign: NavController) {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val context = LocalContext.current.applicationContext
        val profileData = getUserData(firebaseAuth, firestore, context)

        ModalNavigationDrawer(drawerState = drawerState, gesturesEnabled = true, drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .background(Color.Cyan)
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        modifier = Modifier
                            .padding(start = 14.dp, bottom = 2.dp)
                            .size(90.dp),
                        contentDescription = null
                    )
                    Text(
                        text = profileData.firstName+profileData.lastName,
                        modifier = Modifier.padding(start = 20.dp, top = 2.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = profileData.email,
                        modifier = Modifier.padding(start = 20.dp, top = 2.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Setting") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    })
                NavigationDrawerItem(
                    label = { Text(text = "Help and Feedback") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    })
                NavigationDrawerItem(
                    label = { Text(text = "Logout") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        firebaseAuth.signOut()
                        navControllerSign.navigate(LogInItem.AuthScreen.route) {
                            popUpTo(LogInItem.HomeScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }) {
            Scaffold(topBar = {
                ChatHeader(coroutineScope, drawerState)
            }, bottomBar = {
                MainBottomNavigation(navController = navController)
            }) { innerPadding ->
                MainNavigation(
                    navHostController = navController,
                    innerPadding,
                    context,
                    navControllerSign
                )
            }
        }

    }

    @Composable
    fun MainNavigation(
        navHostController: NavHostController,
        padding: PaddingValues,
        context: Context,
        navControllerSign: NavController
    ) {
        NavHost(navController = navHostController, startDestination = BottomNavItem.AItools.route)
        {
            composable(BottomNavItem.AItools.route) {
                MainAiToolScreen(padding, context)
            }
            composable(BottomNavItem.ChatBot.route) {
                ChatScreen(paddingValues = padding)
            }
            composable(BottomNavItem.History.route){
                ToolHistory(paddingValues = padding)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(paddingValues = padding, auth, firestore, navControllerSign, context)
            }
        }
    }

    @Composable
    fun getBitmap(): Bitmap? {
        val uri = uriState.collectAsState().value

        val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current).data(uri).size(Size.ORIGINAL).build()
        ).state

        if (imageState is AsyncImagePainter.State.Success) {
            return imageState.result.drawable.toBitmap()
        }
        return null
    }

    @Composable
    fun ChatScreen(paddingValues: PaddingValues) {
        val chatViewModel = viewModel<ChatViewModel>()
        val chatState = chatViewModel.chatStat.collectAsState().value

        val bitmap = getBitmap()
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(8.dp),
                reverseLayout = true
            ) {
                itemsIndexed(chatState.chatList) { index, chat ->
                    if (chat.isFromUser) {
                        UserChatBox(prompt = chat.prompt, bitmap = chat.bitmap)
                    } else {
                        ModalChatBox(response = chat.prompt)
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    bitmap.let {
                        if (it != null) {
                            Image(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(bottom = 2.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentDescription = "picked image",
                                contentScale = ContentScale.Crop,
                                bitmap = it.asImageBitmap()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = "add photo",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                imagePicker.launch(
                                    PickVisualMediaRequest
                                        .Builder()
                                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        .build()
                                )
                            }, tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    modifier = Modifier.weight(1f),
                    value = chatState.prompt, onValueChange = {
                        chatViewModel.onEvent(ChatUiState.UpdatePrompt(it))
                    },
                    placeholder = {
                        Text(text = "Type a prompt")
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "send message",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            chatViewModel.onEvent(ChatUiState.SendPrompt(chatState.prompt, bitmap))
                            uriState.update { "" }
                        }, tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
