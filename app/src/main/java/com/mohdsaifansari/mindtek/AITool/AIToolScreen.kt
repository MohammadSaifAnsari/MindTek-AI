package com.mohdsaifansari.mindtek.AITool

import android.content.Context
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
import androidx.navigation.NavController
import com.mohdsaifansari.mindtek.AITool.Data.ToolData
import com.mohdsaifansari.mindtek.AITool.Data.ToolItem
import com.mohdsaifansari.mindtek.Components.NavigationItem


@Composable
fun MainAiToolScreen(
    paddingValues: PaddingValues,
    context: Context,
    navControllerSign: NavController
) {


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
            ScrollableCardView(itemSummarizer, context, navControllerSign)
            Text(
                text = "Content Writing Tools",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemContent, context, navControllerSign)
            Text(
                text = "Writer",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemWriter, context, navControllerSign)
            Text(
                text = "Grammar",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemGrammar, context, navControllerSign)
            Text(
                text = "Job Essentials",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
            ScrollableCardView(itemJob, context, navControllerSign)
        }
    }


}


@Composable
fun ScrollableCardView(items: List<String>, context: Context, navControllerSign: NavController) {
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
                        checkTitle(title = title, navControllerSign)
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
                    painter = painterResource(id = toolIcon(item)), contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.End)
                        .padding(end = 5.dp, bottom = 5.dp)
                )
            }
        }
    }
}

fun checkTitle(title: String?, navControllerSign: NavController) {

    if (title == ToolItem.MailGeneration.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.MailGeneration.title,
            ToolItem.MailGeneration.subTitle
        )
    } else if (title == ToolItem.BlogGeneration.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.BlogGeneration.title,
            ToolItem.BlogGeneration.subTitle
        )
    } else if (title == ToolItem.BlogSection.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.BlogSection.title,
            ToolItem.BlogSection.subTitle
        )
    } else if (title == ToolItem.BlogIdeas.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.BlogIdeas.title,
            ToolItem.BlogIdeas.subTitle
        )
    } else if (title == ToolItem.ParagraphGenerator.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.ParagraphGenerator.title,
            ToolItem.ParagraphGenerator.subTitle
        )
    } else if (title == ToolItem.GenerateArticle.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.GenerateArticle.title,
            ToolItem.GenerateArticle.subTitle
        )
    } else if (title == ToolItem.CreativeStory.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.CreativeStory.title,
            ToolItem.CreativeStory.subTitle
        )
    } else if (title == ToolItem.CreativeLetter.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.CreativeLetter.title,
            ToolItem.CreativeLetter.subTitle
        )
    } else if (title == ToolItem.LoveLetter.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.LoveLetter.title,
            ToolItem.LoveLetter.subTitle
        )
    } else if (title == ToolItem.Poems.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.Poems.title,
            ToolItem.Poems.subTitle
        )
    } else if (title == ToolItem.SongLyrics.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.SongLyrics.title,
            ToolItem.SongLyrics.subTitle
        )
    } else if (title == ToolItem.FoodRecipe.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.FoodRecipe.title,
            ToolItem.FoodRecipe.subTitle
        )
    } else if (title == ToolItem.GrammerCorrection.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.GrammerCorrection.title,
            ToolItem.GrammerCorrection.subTitle
        )
    } else if (title == ToolItem.AnswerQuestion.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.AnswerQuestion.title,
            ToolItem.AnswerQuestion.subTitle
        )
    } else if (title == ToolItem.ActivePassive.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.ActivePassive.title,
            ToolItem.ActivePassive.subTitle
        )
    } else if (title == ToolItem.PassiveActive.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.PassiveActive.title,
            ToolItem.PassiveActive.subTitle
        )
    } else if (title == ToolItem.JobDescription.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.JobDescription.title,
            ToolItem.JobDescription.subTitle
        )
    } else if (title == ToolItem.Resume.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.Resume.title,
            ToolItem.Resume.subTitle
        )
    } else if (title == ToolItem.InterviewQuestions.title) {
        navtoToolScreen(
            navControllerSign,
            ToolItem.InterviewQuestions.title,
            ToolItem.InterviewQuestions.subTitle
        )
    } else if (title == ToolItem.TextSummarizer.title) {
        navtoSummarizerScreen(
            navControllerSign,
            ToolItem.TextSummarizer.title,
            ToolItem.TextSummarizer.subTitle
        )
    } else if (title == ToolItem.StorySummarizer.title) {
        navtoSummarizerScreen(
            navControllerSign,
            ToolItem.StorySummarizer.title,
            ToolItem.StorySummarizer.subTitle
        )
    } else if (title == ToolItem.ParagraphSummarizer.title) {
        navtoSummarizerScreen(
            navControllerSign,
            ToolItem.ParagraphSummarizer.title,
            ToolItem.ParagraphSummarizer.subTitle
        )
    }
}

fun toolIcon(title: String): Int {
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

fun navtoToolScreen(navControllerSign: NavController, title: String, subtitle: String) {
    navControllerSign.navigate("Tool_nav/${title}/${subtitle}") {
        popUpTo(NavigationItem.ToolNav.route) {
            inclusive = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun navtoSummarizerScreen(navControllerSign: NavController, title: String, subtitle: String) {
    navControllerSign.navigate("Summarizer_nav/${title}/${subtitle}") {
        popUpTo(NavigationItem.ToolNav.route) {
            inclusive = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

