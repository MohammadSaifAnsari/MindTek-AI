package com.mohdsaifansari.mindtek.History

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mohdsaifansari.mindtek.Components.NavigationItem
import com.mohdsaifansari.mindtek.Data.LoadHistory
import com.mohdsaifansari.mindtek.Database.ToolHistory.ToolHistoryDatabaseProvider
import com.mohdsaifansari.mindtek.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolHistory(paddingValues: PaddingValues, context: Context, navController: NavController) {
    val viewModel: HistoryViewModel = viewModel()
    val historyItems by viewModel.historyData.collectAsState()

    val shimmerLoading by viewModel.isloading.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
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
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        LaunchedEffect(Unit) {
            viewModel.checkToolHistoryData(context, ToolHistoryDatabaseProvider.toolHistoryDatabase)
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(key1 = true) {
                isRefreshing = true
                viewModel.checkToolHistoryData(
                    context,
                    ToolHistoryDatabaseProvider.toolHistoryDatabase
                )
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
        if (shimmerLoading && historyItems.isNotEmpty()) {
            HistoryShimmerListItem(
                isLoading = shimmerLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        } else if (historyItems.isEmpty()) {
            Text(
                text = "No History",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            ScrollableHistoryView(items = historyItems, navController, viewModel, context)
        }


        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = MaterialTheme.colorScheme.primary
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
    val menuList = listOf("Delete", "Copy", "Save")
    var selectedItem by remember { mutableStateOf("") }

    val clipboardManager = LocalClipboardManager.current

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
                        navController.navigate("Result_nav/${item.idNo}") {
                            popUpTo(NavigationItem.ResultNav.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onSurface),
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
                                softWrap = true, fontFamily = FontFamily.SansSerif,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        if ((item.month != "null") && (item.year != "null") && (item.day != "null")) {
                            val text = item.month + " " + item.day + "," + item.year
                            Text(
                                text = text, textAlign = TextAlign.Center, fontSize = 14.sp,
                                modifier = Modifier.padding(3.dp),
                                color = MaterialTheme.colorScheme.onBackground
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
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        DropdownMenu(
                            expanded = expandedDropDownMenu,
                            onDismissRequest = { expandedDropDownMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)
                        ) {
                            menuList.forEach { label ->
                                DropdownMenuItem(text = {
                                    Text(
                                        text = label,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }, onClick = {
                                    selectedItem = label
                                    expandedDropDownMenu = false
                                    if (label == "Delete") {
                                        item.idNo?.let {
                                            viewModel.deleteHistoryItem(
                                                it,
                                                context,
                                                db = ToolHistoryDatabaseProvider.toolHistoryDatabase
                                            )
                                        }
                                    }
                                    if (label == "Copy") {
                                        item.result?.let {
                                            AnnotatedString(
                                                it.substring(1, item.result.length - 1)
                                            )
                                        }?.let { clipboardManager.setText(it) }
                                        Toast.makeText(context, "Copied ", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    if (label == "Save") {
                                        viewModel.saveTextasPdf(
                                            item.result.toString()
                                                .substring(1, item.result.toString().length - 1),
                                            item.idNo.toString().trim(),
                                            context = context
                                        )
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