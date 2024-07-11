package com.mohdsaifansari.mindtek.History

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mohdsaifansari.mindtek.Components.NavigationItem
import com.mohdsaifansari.mindtek.Database.ToolHistory.ToolHistoryDatabaseProvider
import com.mohdsaifansari.mindtek.R

@Composable
fun ResultScreen(navController: NavController, context: Context) {
    val resultFromNav = navController.currentBackStackEntry?.arguments?.getString("result")

    val viewModel: HistoryViewModel = viewModel()
    var result by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        if (resultFromNav != null) {
            result = ToolHistoryDatabaseProvider.toolHistoryDatabase.toolHistoryDao()
                .getToolHistoryById(resultFromNav.toInt())
        }
    }
    Scaffold(topBar = {
        ResultHeader(result.toString(), context, navController)
    }) { innerPadding ->
        ResultMainScreen(result.toString(), paddingValues = innerPadding)
    }

}

@Composable
fun ResultMainScreen(
    resultText: String,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = resultText.substring(1, resultText.length - 1),
                modifier = Modifier.padding(16.dp),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                softWrap = true
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultHeader(resultText: String?, context: Context, navController: NavController) {
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
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = Color.White
        ), navigationIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        navController.navigate(NavigationItem.HomeScreen.route) {
                            popUpTo(NavigationItem.ResultNav.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
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