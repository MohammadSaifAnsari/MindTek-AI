package com.mohdsaifansari.mindtek.ui.theme.ChatBot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ModalChatBox(response: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        modifier = Modifier.padding(start = 8.dp, end = 100.dp, bottom = 22.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.Transparent)
                .clip(RoundedCornerShape(12.dp)),
            text = response, fontSize = 15.sp, color = Color.Black
        )
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserUriChatBox(prompt: String, imageUri: String) {
    Column(
        modifier = Modifier.padding(start = 100.dp, bottom = 11.dp, top = 22.dp)
    ) {

        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardColors(
                containerColor = Color(140, 149, 192, 255),
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            modifier = Modifier.padding(bottom = 22.dp)
        ) {
            if (imageUri.isNotEmpty()) {
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    model = imageUri

                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 12.dp, start = 12.dp, end = 12.dp)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(12.dp)),
                text = prompt, fontSize = 15.sp, color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHeader(
    coroutineScope: CoroutineScope,
    drawerState: androidx.compose.material3.DrawerState
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Mindtek", modifier = Modifier.padding(1.dp),
                fontStyle = FontStyle.Normal, fontWeight = FontWeight.SemiBold
            )
        },
        colors = TopAppBarColors(
            containerColor = Color(220, 226, 241, 255),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            scrolledContainerColor = Color.Black
        ), navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    modifier = Modifier.padding(5.dp),
                    contentDescription = null
                )
            }
        }, actions = {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                modifier = Modifier.padding(5.dp),
                contentDescription = null
            )
        }
    )
}