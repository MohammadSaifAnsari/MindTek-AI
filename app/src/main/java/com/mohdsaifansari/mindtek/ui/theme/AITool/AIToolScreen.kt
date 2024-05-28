package com.mohdsaifansari.mindtek.ui.theme.AITool

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohdsaifansari.mindtek.ui.theme.AITool.Data.ToolData
import com.mohdsaifansari.mindtek.ui.theme.AITool.Data.ToolItem


@Composable
fun MainAiToolScreen(paddingValues: PaddingValues, context: Context) {


    val itemSummarizer = listOf(
        "Text Summarizer",
        "Story Summarizer",
        "Paragraph Summarizer"
    )
    val itemContent = listOf(
        "Mail Generation",
        "Blog Generation",
        "Blog Section",
        "Blog Ideas",
        "Paragraph Generator",
        "Generate Article"
    )
    val itemWriter = listOf(
        "Creative Story",
        "Creative Letter",
        "Love Letter",
        "Poems",
        "Song Lyrics",
        "Food Recipe"
    )
    val itemGrammar = listOf(
        "Grammer Correction",
        "Answer to Question",
        "Active to Passive",
        "Passive to Active"
    )
    val itemJob = listOf(
        "Job Description",
        "Resume",
        "Interview Questions"
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Summarizer", modifier = Modifier.padding(8.dp))
        ScrollableCardView(itemSummarizer,context)
        Text(text = "Content Writing Tools", modifier = Modifier.padding(8.dp))
        ScrollableCardView(itemContent, context)
        Text(text = "Writer", modifier = Modifier.padding(8.dp))
        ScrollableCardView(itemWriter,context)
        Text(text = "Grammar", modifier = Modifier.padding(8.dp))
        ScrollableCardView(itemGrammar,context)
        Text(text = "Job Essentials", modifier = Modifier.padding(8.dp))
        ScrollableCardView(itemJob,context)
    }

}

@Composable
fun ScrollableCardView(items: List<String>) {
    LazyRow {
        items(
            items
        ) { item ->
            Card(
                modifier = Modifier
                    .padding(6.dp)
                    .size(170.dp, 120.dp)
                    .clickable {

                    },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = item, textAlign = TextAlign.Center, fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ScrollableCardView(items: List<String>, context: Context) {
    LazyRow {
        items(
            items
        ) { item ->
            Card(
                modifier = Modifier
                    .padding(6.dp)
                    .size(170.dp, 120.dp)
                    .clickable {
                        val title = ToolData(item).title
                        Log.d("saif123", title.toString())
                        MySwitchCase(title = title, context = context)
                    },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = item, textAlign = TextAlign.Center, fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

fun MySwitchCase(title: String?, context: Context) {

    if (title == ToolItem.MailGeneration.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.MailGeneration.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.MailGeneration.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.BlogGeneration.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.BlogGeneration.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.BlogGeneration.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.BlogSection.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.BlogSection.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.BlogSection.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.BlogIdeas.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.BlogIdeas.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.BlogIdeas.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.ParagraphGenerator.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.ParagraphGenerator.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.ParagraphGenerator.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.GenerateArticle.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.GenerateArticle.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.GenerateArticle.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.CreativeStory.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.CreativeStory.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.CreativeStory.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.CreativeLetter.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.CreativeLetter.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.CreativeLetter.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.LoveLetter.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.LoveLetter.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.LoveLetter.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.Poems.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.Poems.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.Poems.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.SongLyrics.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.SongLyrics.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.SongLyrics.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.FoodRecipe.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.FoodRecipe.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.FoodRecipe.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.GrammerCorrection.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.GrammerCorrection.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.GrammerCorrection.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.AnswerQuestion.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.AnswerQuestion.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.AnswerQuestion.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.ActivePassive.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.ActivePassive.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.ActivePassive.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.PassiveActive.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.PassiveActive.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.PassiveActive.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.JobDescription.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.JobDescription.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.JobDescription.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.Resume.title) {
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.Resume.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.Resume.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.InterviewQuestions.title){
        val intent = Intent(context, ToolsActivity::class.java)
        intent.putExtra("TOOL_TITLE", ToolItem.InterviewQuestions.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.InterviewQuestions.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.TextSummarizer.title){
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.putExtra("SUMMARIZER_TITLE", ToolItem.TextSummarizer.title)
        intent.putExtra("SUMMARIZER_SUBTITLE", ToolItem.TextSummarizer.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.StorySummarizer.title){
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.putExtra("SUMMARIZER_TITLE", ToolItem.StorySummarizer.title)
        intent.putExtra("SUMMARIZER_SUBTITLE", ToolItem.StorySummarizer.subTitle)
        context.startActivity(intent)
    }else if (title == ToolItem.ParagraphSummarizer.title){
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.putExtra("SUMMARIZER_TITLE", ToolItem.ParagraphSummarizer.title)
        intent.putExtra("SUMMARIZER_SUBTITLE", ToolItem.ParagraphSummarizer.subTitle)
        context.startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsHeader(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title, modifier = Modifier.padding(5.dp),
                fontStyle = FontStyle.Normal
            )
        },
        colors = TopAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            scrolledContainerColor = Color.White
        ), navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                modifier = Modifier.padding(5.dp),
                contentDescription = null
            )
        }, actions = {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                modifier = Modifier.padding(5.dp),
                contentDescription = null
            )
        }
    )
}

