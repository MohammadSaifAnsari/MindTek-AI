package com.mohdsaifansari.mindtek

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mohdsaifansari.mindtek.ui.theme.Components.LogInItem
import com.mohdsaifansari.mindtek.ui.theme.Data.LoadHistory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolHistory(paddingValues: PaddingValues, context: Context, navController: NavController) {
    val viewModel: HistoryViewModel = viewModel()
    val historyItems by viewModel.historyData.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
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
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(key1 = true) {
                isRefreshing = true
                viewModel.loadData()
                delay(2000)
                isRefreshing = false
            }
        }
        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }
        ScrollableHistoryView(items = historyItems, navController, viewModel, context)
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = Color(220, 226, 241, 255)
        )
    }
}

@Composable
fun ScrollableHistoryView(
    items: List<LoadHistory>,
    navController: NavController,
    viewModel: HistoryViewModel,
    context: Context
) {
    //Drop Down Menu
    val menuList = listOf("Delete")
    var selectedItem by remember { mutableStateOf("") }

    LazyColumn {
        items(
            items
        ) { item ->
            var expandedDropDownMenu by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("Result_nav/${item.result}") {
                            popUpTo(LogInItem.ResultNav.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color("#F7F7FD".toColorInt())),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.documenticon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .padding(2.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                        item.prompt?.let {
                            Text(
                                text = it.trimStart(),
                                textAlign = TextAlign.Center, fontSize = 18.sp,
                                modifier = Modifier.padding(3.dp),
                                maxLines = 1,
                                softWrap = true, fontFamily = FontFamily.SansSerif
                            )
                        }
                        if ((item.month != "null") && (item.year != "null") && (item.day != "null")) {
                            val text = item.month + " " + item.day + "," + item.year
                            Text(
                                text = text, textAlign = TextAlign.Center, fontSize = 14.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                    }
                    Box(modifier = Modifier.padding(1.dp)) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier
                                .background(Color.Transparent)
                                .clickable { expandedDropDownMenu = !expandedDropDownMenu },
                            tint = Color.Black
                        )
                        DropdownMenu(
                            expanded = expandedDropDownMenu,
                            onDismissRequest = { expandedDropDownMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            menuList.forEach { label ->
                                DropdownMenuItem(text = { Text(text = label) }, onClick = {
                                    selectedItem = label
                                    expandedDropDownMenu = false
                                    if (label == "Delete") {
                                        item.idNo?.let { viewModel.deleteHistoryItem(it, context) }
                                    }
                                })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))

                }


            }

        }
    }

}