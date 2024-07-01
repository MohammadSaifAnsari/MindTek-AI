package com.mohdsaifansari.mindtek.Setting

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohdsaifansari.mindtek.Components.LogInItem
import com.mohdsaifansari.mindtek.R
import com.mohdsaifansari.mindtek.ui.theme.MindtekTheme
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(navController: NavController, context: Context) {

    val themePreference = remember { ThemePreference(context.dataStore) }
    var isDarkTheme by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        themePreference.readThemePreferences().collect {
            isDarkTheme = it
        }
    }
    MindtekTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                SettingHeader(navController = navController)
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
                //start
            ) {

                ThemeSwitch(isDarkTheme) { newTheme ->
                    isDarkTheme = newTheme
                    coroutineScope.launch { themePreference.saveThemePreferences(newTheme) }
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingHeader(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Setting",
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
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        navController.navigate(LogInItem.HomeScreen.route) {
                            popUpTo(LogInItem.SettingNav.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                contentDescription = null
            )
        }
    )
}


@Composable
private fun ThemeSwitch(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {


    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Text(text = "Change Theme", color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { isChecked ->
                onThemeChange(isChecked)
            },
            thumbContent = {
                Icon(
                    imageVector = if (isDarkTheme) {
                        Icons.Default.Check
                    } else {
                        Icons.Default.Clear
                    }, contentDescription = null
                )
            }
        )
    }
}

data class ToggleableInfo(
    val isChecked: Boolean,
    val text: String,
)