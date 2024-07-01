package com.mohdsaifansari.mindtek.AITool

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.mohdsaifansari.mindtek.MainActivity
import com.pspdfkit.document.PdfDocument
import com.pspdfkit.document.PdfDocumentLoader.openDocument


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizerHeader(title: String, context: Context) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title, modifier = Modifier.padding(5.dp),
                fontStyle = FontStyle.Normal,
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
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        context.startActivity(intent)
                    },
                contentDescription = null
            )
        }
    )
}


// on below line we are creating an extract data method to extract our data.
fun extractData(extractedString: MutableState<String>, path: String, context: Context) {
    try {
        // on below line we are creating a variable for storing our extracted text
        var extractedText = ""

        val document: PdfDocument = openDocument(context, Uri.parse(path))
        val count = document.pageCount

        for (i in 0 until count) {
            var eText = document.getPageText(i).trim()
            extractedText = extractedText + "\n" + eText
        }

        // on below line we are setting extracted text to our text view.
        extractedString.value = extractedText
    }
    // on below line we are handling our exception using catch block
    catch (e: Exception) {
        e.printStackTrace()
    }
}