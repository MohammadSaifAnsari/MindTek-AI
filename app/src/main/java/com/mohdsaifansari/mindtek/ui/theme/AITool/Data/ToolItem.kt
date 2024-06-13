package com.mohdsaifansari.mindtek.ui.theme.AITool.Data

import com.mohdsaifansari.mindtek.R

sealed class ToolItem(
    val title: String,
    val subTitle: String,
    val toolPrompt: String,
    val image: Int,
    val backgroundColor: String
) {

    //Summarizer
    object TextSummarizer : ToolItem(
        title = "Text Summarizer",
        subTitle = "Key ideas from lengthy text.",
        toolPrompt = "Summarize the following text in a concise manner, capturing the main points and key information: ",
        image = R.drawable.text,
        backgroundColor = "#DEE2F7"
    )

    object StorySummarizer : ToolItem(
        title = "Story Summarizer",
        subTitle = " Plot, characters, and themes in a nutshell.",
        toolPrompt = "Summarize the plot of this story, focusing on the main events and characters:",
        image = R.drawable.story,
        backgroundColor = "#DEE2F7"
    )

    object ParagraphSummarizer : ToolItem(
        title = "Paragraph Summarizer",
        subTitle = "Main idea and key points extracted.",
        toolPrompt = "Provide a brief summary of the key points in this paragraph: ",
        image = R.drawable.paragraph,
        backgroundColor = "#DEE2F7"
    )


    //Content Writing Tools
    object MailGeneration : ToolItem(
        title = "Mail Generation",
        subTitle = "Write a professional email in a few seconds.",
        toolPrompt = " Please draft a email on",
        image = R.drawable.mail,
        backgroundColor = "#DEE2F7"
    )

    object BlogGeneration : ToolItem(
        title = "Blog Generation",
        subTitle = "Create a compelling blog post in minutes.",
        toolPrompt = " Write a blog post on the topic",
        image = R.drawable.blog,
        backgroundColor = "#DEE2F7"
    )

    object BlogSection : ToolItem(
        title = "Blog Section",
        subTitle = "Generate a well-structured blog section on any topic.",
        toolPrompt = "Create a section for a blog post titled",
        image = R.drawable.blog,
        backgroundColor = "#DEE2F7"
    )

    object BlogIdeas : ToolItem(
        title = "Blog Ideas",
        subTitle = "Get inspired with unique and engaging blog post ideas.",
        toolPrompt = "Provide some blog post ideas for a ",
        image = R.drawable.blog,
        backgroundColor = "#DEE2F7"
    )

    object ParagraphGenerator : ToolItem(
        title = "Paragraph Generator",
        subTitle = "Write a high-quality paragraph on any topic.",
        toolPrompt = "Generate a paragraph explaining the",
        image = R.drawable.paragraph,
        backgroundColor = "#DEE2F7"
    )

    object GenerateArticle : ToolItem(
        title = "Generate Article",
        subTitle = "Create a comprehensive article on any topic.",
        toolPrompt = "Write a detailed article about the",
        image = R.drawable.article,
        backgroundColor = "#DEE2F7"
    )

    //Writer
    object CreativeStory : ToolItem(
        title = "Creative Story",
        subTitle = "Let your imagination run wild.",
        toolPrompt = "Write a story ",
        image = R.drawable.creativestory,
        backgroundColor = "#DEE2F7"
    )

    object CreativeLetter : ToolItem(
        title = "Creative Letter",
        subTitle = "Express yourself in a unique way.",
        toolPrompt = "Write a brief creative letter",
        image = R.drawable.letter,
        backgroundColor = "#DEE2F7"
    )

    object LoveLetter : ToolItem(
        title = "Love Letter",
        subTitle = "Pour your heart out.",
        toolPrompt = "Compose a love letter ",
        image = R.drawable.letter,
        backgroundColor = "#DEE2F7"
    )

    object Poems : ToolItem(
        title = "Poems",
        subTitle = "Find the perfect words to express your emotions.",
        toolPrompt = "Write a poem about",
        image = R.drawable.poem,
        backgroundColor = "#DEE2F7"
    )

    object SongLyrics : ToolItem(
        title = "Song Lyrics",
        subTitle = "Create a catchy tune.",
        toolPrompt = "Create lyrics for a song about",
        image = R.drawable.songlyrics,
        backgroundColor = "#DEE2F7"
    )

    object FoodRecipe : ToolItem(
        title = "Food Recipe",
        subTitle = "Cook up something delicious.",
        toolPrompt = " Invent a recipe for a dish that",
        image = R.drawable.foodrecipe,
        backgroundColor = "#DEE2F7"
    )


    //Grammar
    object GrammerCorrection : ToolItem(
        title = "Grammer Correction",
        subTitle = "Let's fix your grammar.",
        toolPrompt = "Rewrite the following sentence with proper grammar.",
        image = R.drawable.paragraph,
        backgroundColor = "#DEE2F7"
    )

    object AnswerQuestion : ToolItem(
        title = "Answer to Question",
        subTitle = "I'll answer that for you.",
        toolPrompt = "Provide a response to the given question",
        image = R.drawable.answertoquestion,
        backgroundColor = "#DEE2F7"
    )

    object ActivePassive : ToolItem(
        title = "Active to Passive",
        subTitle = "Let's make it passive.",
        toolPrompt = "Transform the following active voice sentence into passive voice.",
        image = R.drawable.activetopassive,
        backgroundColor = "#DEE2F7"
    )

    object PassiveActive : ToolItem(
        title = "Passive to Active",
        subTitle = "Let's make it active.",
        toolPrompt = "Rewrite the following passive voice sentence into active voice.",
        image = R.drawable.activetopassive,
        backgroundColor = "#DEE2F7"
    )


    //Job Essentials
    object JobDescription : ToolItem(
        title = "Job Description",
        subTitle = "Find the perfect fit for your team.",
        toolPrompt = "Write a job description",
        image = R.drawable.job,
        backgroundColor = "#DEE2F7"
    )

    object Resume : ToolItem(
        title = "Resume",
        subTitle = "Showcase your skills and experience.",
        toolPrompt = "Create a resume for",
        image = R.drawable.resume,
        backgroundColor = "#DEE2F7"
    )

    object InterviewQuestions : ToolItem(
        title = "Interview Questions",
        subTitle = "Prepare for your next interview.",
        toolPrompt = "Develop a list of interview questions for a candidate",
        image = R.drawable.interviewquestion,
        backgroundColor = "#DEE2F7"
    )
}