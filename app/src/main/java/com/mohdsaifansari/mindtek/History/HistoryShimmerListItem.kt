package com.mohdsaifansari.mindtek.History

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.mohdsaifansari.mindtek.Components.shimmerEffect

@Composable
fun HistoryShimmerListItem(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        LazyColumn {
            items(10) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(60.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.onSurface),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shimmerEffect()
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .height(15.dp)
                                    .shimmerEffect(),
                            )
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(0.7f)
                                    .height(15.dp)
                                    .shimmerEffect()
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}

