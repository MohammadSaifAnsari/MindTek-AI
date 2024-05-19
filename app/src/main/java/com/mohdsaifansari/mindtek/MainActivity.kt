package com.mohdsaifansari.mindtek

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.mohdsaifansari.mindtek.ui.theme.AImageScreen
import com.mohdsaifansari.mindtek.ui.theme.AiToolScreen
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.ChatUiState
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.ChatViewModel
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.ModalChatBox
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.UserChatBox
import com.mohdsaifansari.mindtek.ui.theme.ChatBotAi.ChatHeader
import com.mohdsaifansari.mindtek.ui.theme.Components.BottomNavItem
import com.mohdsaifansari.mindtek.ui.theme.Components.MainBottomNavigation
import com.mohdsaifansari.mindtek.ui.theme.MindtekTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindtekTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainEntryPoint()
                }

            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainEntryPoint(){
        val navController = rememberNavController()
        Scaffold(topBar = {
            ChatHeader()
        }, bottomBar = {
            MainBottomNavigation(navController = navController)
        }) {innerPadding->
            MainNavigation(navHostController = navController,innerPadding)
        }
    }
    @Composable
    fun MainNavigation(navHostController : NavHostController, padding:PaddingValues){
        NavHost(navController = navHostController, startDestination = BottomNavItem.AItools.route )
        {
            composable(BottomNavItem.AItools.route){
                AiToolScreen()
            }
            composable(BottomNavItem.ChatBot.route){
                ChatScreen(paddingValues = padding)
            }
            composable(BottomNavItem.AImages.route){
                AImageScreen()
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
            modifier = Modifier.padding(paddingValues)
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
