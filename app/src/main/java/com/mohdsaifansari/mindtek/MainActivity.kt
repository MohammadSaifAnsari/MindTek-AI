package com.mohdsaifansari.mindtek

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
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
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohdsaifansari.mindtek.AITool.MainAiToolScreen
import com.mohdsaifansari.mindtek.AITool.MainSummarizerScreen
import com.mohdsaifansari.mindtek.AITool.MainToolScreen
import com.mohdsaifansari.mindtek.Account.EditProfile
import com.mohdsaifansari.mindtek.Account.ProfileItemScreen
import com.mohdsaifansari.mindtek.Account.ProfilePhotoKey
import com.mohdsaifansari.mindtek.Account.ProfilePicture
import com.mohdsaifansari.mindtek.Account.ProfileScreen
import com.mohdsaifansari.mindtek.Account.ProfileViewModel
import com.mohdsaifansari.mindtek.AdsMob.CoinViewModal
import com.mohdsaifansari.mindtek.AdsMob.rewardedAds
import com.mohdsaifansari.mindtek.Authentication.SignInScreen
import com.mohdsaifansari.mindtek.Authentication.SignUpScreen
import com.mohdsaifansari.mindtek.ChatBot.ChatUiState
import com.mohdsaifansari.mindtek.ChatBot.ChatViewModel
import com.mohdsaifansari.mindtek.ChatBot.MainHeader
import com.mohdsaifansari.mindtek.ChatBot.ModalChatBox
import com.mohdsaifansari.mindtek.ChatBot.UserUriChatBox
import com.mohdsaifansari.mindtek.Components.BottomNavItem
import com.mohdsaifansari.mindtek.Components.LoadingAnimation
import com.mohdsaifansari.mindtek.Components.NavigationItem
import com.mohdsaifansari.mindtek.Components.MainBottomNavigation
import com.mohdsaifansari.mindtek.Data.DrawerItem
import com.mohdsaifansari.mindtek.Database.ChatDatabaseProvider
import com.mohdsaifansari.mindtek.Database.DatabaseProvider
import com.mohdsaifansari.mindtek.Database.ToolHistory.ToolHistoryDatabaseProvider
import com.mohdsaifansari.mindtek.History.ResultScreen
import com.mohdsaifansari.mindtek.History.ToolHistory
import com.mohdsaifansari.mindtek.Setting.SettingScreen
import com.mohdsaifansari.mindtek.Setting.ThemeChange.ThemePreference
import com.mohdsaifansari.mindtek.Setting.ThemeChange.dataStore
import com.mohdsaifansari.mindtek.ui.theme.MindtekTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    private val uriState = MutableStateFlow("")
    val imagePicker = registerForActivityResult<PickVisualMediaRequest, Uri?>(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            uriState.update {
                uri.toString()
            }
        }

    }

    private val pdfUri = MutableStateFlow("")

    private val launcher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            pdfUri.update {
                uri.toString()
            }
        }

    }
    val context = this@MainActivity
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val firebaseAuth = FirebaseAuth.getInstance()
    var isDarkTheme by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        var keepSplashScreenVisible = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreenVisible }

        lifecycleScope.launch {
            val themePreference = ThemePreference(context.dataStore)
            isDarkTheme = themePreference.readThemePreferences().first()
            delay(1000)
            keepSplashScreenVisible = false
        }


        // Copy spiece.model from assets to a file in the cache directory.
        val modelFile = File(cacheDir, "spiece.model")
        if (!modelFile.exists()) {
            copyAssetToFile(assets, "spiece.model", modelFile)
        }
        Log.d("SummarizerModel", "Model file path: ${modelFile.absolutePath}")


        // Load the SentencePiece model using the file path.
        if (!SentencePieceTokenizer.loadModel(modelFile.absolutePath)) {
            Log.e("SummarizerModel", "Failed to load SentencePiece model")
        } else {
            Log.d("SummarizerModel", "SentencePiece model loaded successfully")
        }
        val isGpuSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_OPENGLES_EXTENSION_PACK)
        Log.d("DeviceCheck", "GPU delegate supported: $isGpuSupported")



        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        setContent {
            val themePreference = remember { ThemePreference(context.dataStore) }
            LaunchedEffect(Unit) {
                themePreference.readThemePreferences().collect {
                    isDarkTheme = it
                }
            }
            MindtekTheme(darkTheme = isDarkTheme) {
                MobileAds.initialize(this)
                DatabaseProvider.initialize(this@MainActivity)
                ChatDatabaseProvider.initialize(this@MainActivity)
                ToolHistoryDatabaseProvider.initialize(this@MainActivity)
                val viewModel: ProfileViewModel = viewModel()
                val navControllerSign = rememberNavController()
                NavHost(
                    navController = navControllerSign,
                    startDestination = if (firebaseAuth.currentUser != null) {
                        NavigationItem.HomeScreen.route
                    } else {
                        NavigationItem.AuthScreen.route
                    }
                ) {
                    navigation(
                        startDestination = NavigationItem.SignIn.route,
                        route = NavigationItem.AuthScreen.route
                    ) {
                        composable(NavigationItem.SignIn.route) {
                            SignInScreen(auth = auth, context = context, navControllerSign)
                        }
                        composable(NavigationItem.SignUp.route) {
                            SignUpScreen(auth = auth, context = context, navControllerSign)
                        }
                    }
                    navigation(
                        startDestination = NavigationItem.MainScreen.route,
                        route = NavigationItem.HomeScreen.route
                    ) {
                        composable(NavigationItem.MainScreen.route) {
                            MainEntryPoint(context, navControllerSign)
                        }
                    }
                    navigation(
                        startDestination = NavigationItem.ProfileEditScreen.route,
                        route = NavigationItem.ProfileEditNav.route
                    ) {
                        composable(NavigationItem.ProfileEditScreen.route) {
                            EditProfile(
                                context,
                                imagePicker,
                                uriState,
                                viewModel,
                                db = DatabaseProvider.userDatabase,
                                navControllerSign
                            )
                        }
                    }
                    navigation(
                        startDestination = NavigationItem.ResultScreen.route,
                        route = NavigationItem.ResultNav.route
                    ) {
                        composable(NavigationItem.ResultScreen.route) {
                            ResultScreen(navControllerSign, context)
                        }
                    }
                    navigation(
                        startDestination = NavigationItem.ProfileItemScreen.route,
                        route = NavigationItem.ProfileItemNav.route
                    ) {
                        composable(NavigationItem.ProfileItemScreen.route) {
                            ProfileItemScreen(navController = navControllerSign, context = context)
                        }
                    }
                    navigation(
                        startDestination = NavigationItem.SettingScreen.route,
                        route = NavigationItem.SettingNav.route
                    ) {
                        composable(NavigationItem.SettingScreen.route) {
                            SettingScreen(navController = navControllerSign, context = context)
                        }
                    }
                    navigation(
                        startDestination = NavigationItem.SummarizerScreen.route,
                        route = NavigationItem.SummarizerNav.route
                    ) {
                        composable(NavigationItem.SummarizerScreen.route) {
                            MainSummarizerScreen(navController = navControllerSign,
                                context,
                                pdfUri,
                                launcher,
                                clearPdfUri = { clearpdfUri ->
                                    pdfUri.update { clearpdfUri }
                                },
                                modelFile.absolutePath,
                                assets)
                        }
                    }
                    navigation(
                        startDestination = NavigationItem.ToolScreen.route,
                        route = NavigationItem.ToolNav.route
                    ) {
                        composable(NavigationItem.ToolScreen.route) {
                            MainToolScreen(navController = navControllerSign, context = context)
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

        val viewModel: ProfileViewModel = viewModel()
        val profileData by viewModel.userData.collectAsState()


        val drawerItemList = listOf(
            DrawerItem(R.drawable.setting, "Setting"),
            DrawerItem(R.drawable.logout, "Logout")
        )

        var isDialog by remember {
            mutableStateOf(false)
        }

        var coin by remember {
            mutableStateOf("0")
        }

        var circularLoading by remember {
            mutableStateOf(false)
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    drawerContentColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.systemBarsPadding()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
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
                        LaunchedEffect(Unit) {
                            viewModel.checkUserData(
                                context = context,
                                db = DatabaseProvider.userDatabase
                            )
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        ProfilePicture(
                            viewModel = viewModel,
                            profileKey = ProfilePhotoKey.NavDrawerPhotoKey.name
                        )

                        profileData?.let {
                            Text(
                                text = it.firstName + " " + it.lastName,
                                modifier = Modifier.padding(start = 22.dp, top = 2.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = it.email,
                                modifier = Modifier.padding(start = 22.dp, top = 2.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    HorizontalDivider()
                    drawerItemList.forEach { item ->
                        Spacer(modifier = Modifier.height(4.dp))
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = item.title,
                                    color = if (item.title == "Logout") Color.Red else MaterialTheme.colorScheme.onBackground,
                                    fontFamily = FontFamily.Serif
                                )
                            },
                            selected = false,
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                                if (item.title == "Logout") {
                                    firebaseAuth.signOut()
                                    navControllerSign.navigate(NavigationItem.AuthScreen.route) {
                                        popUpTo(NavigationItem.HomeScreen.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else if (item.title == "Setting") {
                                    navControllerSign.navigate(NavigationItem.SettingNav.route) {
                                        popUpTo(NavigationItem.HomeScreen.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = MaterialTheme.colorScheme.background,
                                selectedContainerColor = MaterialTheme.colorScheme.primary
                            ),
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .background(Color.Transparent)
                                        .size(24.dp),
                                    tint = if (item.title == "Logout") Color.Red else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        )
                    }
                }
            }) {
            val coinViewModal = viewModel<CoinViewModal>()
            coin = coinViewModal.isCoin.collectAsState().value.toString()
            Scaffold(topBar = {
                MainHeader(coroutineScope, drawerState, coin) { onClicked ->
                    isDialog = onClicked
                }
            }, bottomBar = {
                MainBottomNavigation(navController = navController)
            }) { innerPadding ->
                MainBottomNavigation(
                    navHostController = navController,
                    innerPadding,
                    context,
                    navControllerSign,
                    viewModel
                )
                if (isDialog) {
                    Dialog(onDismissRequest = {
                        isDialog = false
                    }) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                            modifier = Modifier.size(200.dp, 200.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Watch Video")
                                Icon(
                                    painter = painterResource(id = R.drawable.watchvideo),
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(50.dp)
                                        .background(Color.Transparent),
                                    contentDescription = null, tint = Color.Unspecified
                                )
                                Button(
                                    onClick = {
                                        circularLoading = true
                                        rewardedAds(
                                            this@MainActivity,
                                            coinViewModal,
                                            closeDialogbox = {
                                                isDialog = it
                                            },
                                            closeCircularloading = {
                                                circularLoading = it
                                            })
                                    }, colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                ) {
                                    if (circularLoading) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    } else {
                                        Text(
                                            text = "Earn 10 Coins",
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

    }

    @Composable
    fun MainBottomNavigation(
        navHostController: NavHostController,
        padding: PaddingValues,
        context: Context,
        navControllerSign: NavController,
        viewModel: ProfileViewModel
    ) {
        NavHost(navController = navHostController, startDestination = BottomNavItem.AItools.route)
        {
            composable(BottomNavItem.AItools.route) {
                MainAiToolScreen(padding, context, navControllerSign)
            }
            composable(BottomNavItem.ChatBot.route) {
                ChatScreen(paddingValues = padding)
            }
            composable(BottomNavItem.History.route) {
                ToolHistory(paddingValues = padding, context, navControllerSign)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    paddingValues = padding,
                    auth,
                    navControllerSign,
                    context,
                    viewModel
                )
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
        val chatState = chatViewModel.chatstate.collectAsState().value


        val bitmap = getBitmap()
        val uri = uriState.collectAsState().value
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary, // #dee4f4
                            MaterialTheme.colorScheme.background
                        ), start = Offset(0f, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            LaunchedEffect(Unit) {
                chatViewModel.checkChatData(context, db = ChatDatabaseProvider.chatDatabase)
            }
            if (chatViewModel.iscircularloading.collectAsState().value) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(8.dp),
                    reverseLayout = true
                ) {
                    itemsIndexed(chatState.chatList) { index, chat ->
                        if (chat != null) {
                            if (chat.isUser) {
                                UserUriChatBox(
                                    prompt = chat.message,
                                    imageUri = chat.imageAddress,
                                    timestamp = chat.timestamp.toString(),
                                    timeAndDate = chatViewModel.TimestampToTime(chat.timestamp),
                                    context = context,
                                    chatViewModel = chatViewModel,
                                    reload = {
                                        chatViewModel.viewModelScope.launch {
                                            withContext(Dispatchers.IO) {
                                                ChatDatabaseProvider.chatDatabase.chatDao()
                                                    .deleteChatByTimestamp(chat.timestamp)
                                                chatViewModel.checkChatData(
                                                    context,
                                                    db = ChatDatabaseProvider.chatDatabase
                                                )
                                            }
                                        }
                                    }
                                )
                            } else {
                                ModalChatBox(
                                    response = chat.message,
                                    timeAndDate = chatViewModel.TimestampToTime(chat.timestamp),
                                    timestamp = chat.timestamp.toString(),
                                    context = context,
                                    chatViewModel = chatViewModel,
                                    reload = {
                                        chatViewModel.viewModelScope.launch {
                                            withContext(Dispatchers.IO) {
                                                ChatDatabaseProvider.chatDatabase.chatDao()
                                                    .deleteChatByTimestamp(chat.timestamp)
                                                chatViewModel.checkChatData(
                                                    context,
                                                    db = ChatDatabaseProvider.chatDatabase
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            if (chatViewModel.isloadingAnimation.collectAsState().value) {
                LoadingAnimation(circleSize = 14.dp, spaceBetween = 6.dp, travelDistance = 12.dp)
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

                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = chatState.prompt, onValueChange = {
                        chatViewModel.onEvent(ChatUiState.UpdatePrompt(it))
                    },
                    placeholder = {
                        Text(text = "Type a prompt")
                    }, trailingIcon = {
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
                                }, tint = MaterialTheme.colorScheme.secondary
                        )
                    }, colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        cursorColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.sendcircleicon),
                    contentDescription = "send message",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            chatViewModel.onEvent(
                                ChatUiState.SendPrompt(
                                    chatState.prompt,
                                    bitmap,
                                    uri
                                )
                            )
                            uriState.update { "" }
                        }
                        .background(Color.Transparent), tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
// Helper function to copy asset file to a destination file.
private fun copyAssetToFile(assetManager: AssetManager, assetName: String, outFile: File) {
    assetManager.open(assetName).use { inputStream ->
        FileOutputStream(outFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    Log.d("SummarizerModel", "Copied $assetName to ${outFile.absolutePath}")
}
