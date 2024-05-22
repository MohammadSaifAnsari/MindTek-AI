package com.mohdsaifansari.mindtek.ui.theme.AITool

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohdsaifansari.mindtek.ui.theme.MindtekTheme

class ToolsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindtekTheme {
                val tool_title = intent.getStringExtra("TOOL_TITLE")
                val tool_subtitle = intent.getStringExtra("TOOL_SUBTITLE")
                Generation(tool_title.toString(),tool_subtitle.toString())
                Log.d("saif123", tool_title.toString()+tool_subtitle.toString())
            }
        }
    }
}

@Composable
fun Generation(title:String,subTitle:String){
    var text by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            ToolsHeader(title)
        }
    ){innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = subTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(250.dp),
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                placeholder = {
                    Text(text = "Type a prompt")
                },
                shape = RoundedCornerShape(16.dp),
            )

            Button(modifier = Modifier
                .align(Alignment.End)
                .padding(end = 20.dp, top = 20.dp),
                onClick = {
                /*TODO*/
                }) {
                Text(text = "Generate")
            }
        }

    }
}