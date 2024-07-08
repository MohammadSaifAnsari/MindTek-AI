package com.mohdsaifansari.mindtek.ChatBot

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mohdsaifansari.mindtek.AdsMob.CoinViewModal
import com.mohdsaifansari.mindtek.AdsMob.rewardedAds
import com.mohdsaifansari.mindtek.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ModalChatBox(response: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.onBackground,
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
            text = response, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground
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
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSurface,
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
                text = prompt, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHeader(
    coroutineScope: CoroutineScope,
    drawerState: androidx.compose.material3.DrawerState,
    coin: String,
    onClicked: (Boolean) -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Mindtek", modifier = Modifier.padding(1.dp),
                fontStyle = FontStyle.Normal, fontWeight = FontWeight.SemiBold
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = MaterialTheme.colorScheme.onBackground
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
            Row(
                modifier = Modifier
                    .padding(1.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.coinimg),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(36.dp)
                        .background(Color.Transparent),
                    contentDescription = null, tint = Color.Unspecified
                )
                Row(
                    modifier = Modifier
                        .padding(start = 1.dp)
                        .size(70.dp, 40.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = coin,
                            modifier = Modifier
                                .padding(start = 1.dp),
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    IconButton(onClick = {
                        onClicked(true)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(24.dp),
                            contentDescription = null
                        )
                    }
                }


            }
        }
    )
}