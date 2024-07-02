package com.mohdsaifansari.mindtek.AITool

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.mohdsaifansari.mindtek.AITool.Data.ToolData
import com.mohdsaifansari.mindtek.AITool.Data.ToolItem


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
        "Song Lyrics",
        "Food Recipe",
        "Poems",
        "Love Letter"
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
                text = "Summarizer",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemSummarizer, context)
            Text(
                text = "Content Writing Tools",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemContent, context)
            Text(
                text = "Writer",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemWriter, context)
            Text(
                text = "Grammar",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemGrammar, context)
            Text(
                text = "Job Essentials",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemJob, context)
        }
    }


}


@Composable
fun ScrollableCardView(items: List<String>, context: Context) {
    LazyRow {
        items(
            items
        ) { item ->
            val hexadecimal = "#D2D8EE"
            Card(
                modifier = Modifier
                    .padding(6.dp)
                    .size(190.dp, 130.dp)
                    .clickable {
                        val title = ToolData(item).title
                        MySwitchCase(title = title, context = context)
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Text(
                    text = item,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily.Serif
                )
                Image(
                    painter = painterResource(id = ToolIcon(item)), contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.End)
                        .padding(end = 5.dp, bottom = 5.dp)
                )
            }
        }
    }
}

fun MySwitchCase(title: String?, context: Context) {

    if (title == ToolItem.MailGeneration.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.MailGeneration.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.MailGeneration.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.BlogGeneration.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.BlogGeneration.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.BlogGeneration.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.BlogSection.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.BlogSection.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.BlogSection.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.BlogIdeas.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.BlogIdeas.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.BlogIdeas.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.ParagraphGenerator.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.ParagraphGenerator.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.ParagraphGenerator.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.GenerateArticle.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.GenerateArticle.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.GenerateArticle.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.CreativeStory.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.CreativeStory.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.CreativeStory.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.CreativeLetter.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.CreativeLetter.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.CreativeLetter.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.LoveLetter.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.LoveLetter.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.LoveLetter.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.Poems.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.Poems.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.Poems.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.SongLyrics.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.SongLyrics.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.SongLyrics.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.FoodRecipe.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.FoodRecipe.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.FoodRecipe.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.GrammerCorrection.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.GrammerCorrection.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.GrammerCorrection.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.AnswerQuestion.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.AnswerQuestion.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.AnswerQuestion.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.ActivePassive.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.ActivePassive.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.ActivePassive.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.PassiveActive.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.PassiveActive.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.PassiveActive.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.JobDescription.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.JobDescription.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.JobDescription.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.Resume.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.Resume.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.Resume.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.InterviewQuestions.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.InterviewQuestions.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.InterviewQuestions.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.TextSummarizer.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.TextSummarizer.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.TextSummarizer.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.StorySummarizer.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.StorySummarizer.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.StorySummarizer.subTitle)
        context.startActivity(intent)
    } else if (title == ToolItem.ParagraphSummarizer.title) {
        val intent = Intent(context, SummarizerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("TOOL_TITLE", ToolItem.ParagraphSummarizer.title)
        intent.putExtra("TOOL_SUBTITLE", ToolItem.ParagraphSummarizer.subTitle)
        context.startActivity(intent)
    }
}

fun ToolIcon(title: String): Int {
    if (title == ToolItem.MailGeneration.title) {
        return ToolItem.MailGeneration.image
    } else if (title == ToolItem.BlogGeneration.title) {
        return ToolItem.BlogGeneration.image
    } else if (title == ToolItem.BlogSection.title) {
        return ToolItem.BlogSection.image
    } else if (title == ToolItem.BlogIdeas.title) {
        return ToolItem.BlogIdeas.image
    } else if (title == ToolItem.ParagraphGenerator.title) {
        return ToolItem.ParagraphGenerator.image
    } else if (title == ToolItem.GenerateArticle.title) {
        return ToolItem.GenerateArticle.image
    } else if (title == ToolItem.CreativeStory.title) {
        return ToolItem.CreativeStory.image
    } else if (title == ToolItem.CreativeLetter.title) {
        return ToolItem.CreativeLetter.image
    } else if (title == ToolItem.LoveLetter.title) {
        return ToolItem.LoveLetter.image
    } else if (title == ToolItem.Poems.title) {
        return ToolItem.Poems.image
    } else if (title == ToolItem.SongLyrics.title) {
        return ToolItem.SongLyrics.image
    } else if (title == ToolItem.FoodRecipe.title) {
        return ToolItem.FoodRecipe.image
    } else if (title == ToolItem.GrammerCorrection.title) {
        return ToolItem.GrammerCorrection.image
    } else if (title == ToolItem.AnswerQuestion.title) {
        return ToolItem.AnswerQuestion.image
    } else if (title == ToolItem.ActivePassive.title) {
        return ToolItem.ActivePassive.image
    } else if (title == ToolItem.PassiveActive.title) {
        return ToolItem.PassiveActive.image
    } else if (title == ToolItem.JobDescription.title) {
        return ToolItem.JobDescription.image
    } else if (title == ToolItem.Resume.title) {
        return ToolItem.Resume.image
    } else if (title == ToolItem.InterviewQuestions.title) {
        return ToolItem.InterviewQuestions.image
    } else if (title == ToolItem.TextSummarizer.title) {
        return ToolItem.TextSummarizer.image
    } else if (title == ToolItem.StorySummarizer.title) {
        return ToolItem.StorySummarizer.image
    } else {
        return ToolItem.ParagraphSummarizer.image
    }
}

