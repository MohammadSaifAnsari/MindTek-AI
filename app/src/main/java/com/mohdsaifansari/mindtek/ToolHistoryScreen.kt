package com.mohdsaifansari.mindtek

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohdsaifansari.mindtek.ui.theme.Data.loadHistory

@Composable
fun ToolHistory(paddingValues: PaddingValues, context: Context) {
    val viewModel: HistoryViewModel = viewModel()
    Column(
        modifier = Modifier.padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScrollableHistoryView(items = viewModel.loadData(), context = context)
    }
}

@Composable
fun ScrollableHistoryView(items: List<loadHistory>, context: Context) {
    LazyColumn {
        items(
            items
        ) { item ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {

                    },
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Default.AccountBox, contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.Transparent), tint = Color.Black
                    )
                    Column(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(0.9f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.prompt,
                            textAlign = TextAlign.Center, fontSize = 18.sp,
                            modifier = Modifier.padding(4.dp),
                            maxLines = 1,
                            softWrap = false
                        )
                        val text = item.month + " " + item.day + "," + item.year
                        Text(
                            text = text, textAlign = TextAlign.Center, fontSize = 14.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Icon(
                        Icons.Default.MoreVert, contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Transparent), tint = Color.Black
                    )
                }

            }
        }
    }
}