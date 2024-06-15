package com.mohdsaifansari.mindtek

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ResultScreen(navController: NavController, context: Context) {
    val result = navController.currentBackStackEntry?.arguments?.getString("result")
    Scaffold(topBar = {
        ResultHeader(result, context)
    }) { innerPadding ->
        ResultMainScreen(result, paddingValues = innerPadding)
    }

}

@Composable
fun ResultMainScreen(
    resultText: String?,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFDCE2F1), // #dee4f4
                        Color(0xFFFFFFFF)
                    ), start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (resultText != null) {
                Text(
                    text = resultText, modifier = Modifier.padding(16.dp),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    softWrap = true
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultHeader(resultText: String?, context: Context) {
    val clipboardManager = LocalClipboardManager.current
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Result",
                modifier = Modifier.padding(5.dp),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
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
                imageVector = Icons.Default.KeyboardArrowLeft,
                modifier = Modifier.padding(5.dp),
                contentDescription = null
            )
        }, actions = {
            Icon(
                painter = painterResource(id = R.drawable.copytext),
                modifier = Modifier
                    .padding(5.dp)
                    .size(24.dp)
                    .clickable {
                        if (resultText != null) {
                            clipboardManager.setText(AnnotatedString(resultText))
                            Toast
                                .makeText(context, "Copied ", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                contentDescription = null
            )
        }
    )
}