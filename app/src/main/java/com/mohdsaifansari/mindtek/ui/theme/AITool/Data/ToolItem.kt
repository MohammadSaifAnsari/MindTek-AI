package com.mohdsaifansari.mindtek.ui.theme.AITool.Data

sealed class ToolItem(val title:String,val subTitle:String,val toolPrompt:String) {

    //Summarizer
    object TextSummarizer : ToolItem(title = "Text Summarizer", subTitle = "Key ideas from lengthy text.", toolPrompt = "Summarize the following text in a concise manner, capturing the main points and key information: ")
    object StorySummarizer : ToolItem(title = "Story Summarizer", subTitle = " Plot, characters, and themes in a nutshell.", toolPrompt = "Summarize the plot of this story, focusing on the main events and characters:")
    object ParagraphSummarizer : ToolItem(title = "Paragraph Summarizer", subTitle = "Main idea and key points extracted.", toolPrompt = "Provide a brief summary of the key points in this paragraph: ")


    //Content Writing Tools
    object MailGeneration : ToolItem(title = "Mail Generation", subTitle = "Write a professional email in a few seconds.", toolPrompt = " Please draft a email on")
    object BlogGeneration : ToolItem(title = "Blog Generation", subTitle = "Create a compelling blog post in minutes.", toolPrompt = " Write a blog post on the topic")
    object BlogSection : ToolItem(title = "Blog Section", subTitle = "Generate a well-structured blog section on any topic.", toolPrompt = "Create a section for a blog post titled")
    object BlogIdeas : ToolItem(title = "Blog Ideas", subTitle = "Get inspired with unique and engaging blog post ideas.", toolPrompt = "Provide some blog post ideas for a ")
    object ParagraphGenerator : ToolItem(title = "Paragraph Generator", subTitle = "Write a high-quality paragraph on any topic.", toolPrompt = "Generate a paragraph explaining the")
    object GenerateArticle : ToolItem(title = "Generate Article", subTitle = "Create a comprehensive article on any topic.", toolPrompt = "Write a detailed article about the")

    //Writer
    object CreativeStory : ToolItem(title = "Creative Story", subTitle = "Let your imagination run wild.", toolPrompt = "Write a story " )
    object CreativeLetter : ToolItem(title = "Creative Letter", subTitle = "Express yourself in a unique way.", toolPrompt = "Write a brief creative letter" )
    object LoveLetter : ToolItem(title = "Love Letter", subTitle = "Pour your heart out.", toolPrompt = "Compose a love letter " )
    object Poems : ToolItem(title = "Poems", subTitle = "Find the perfect words to express your emotions.", toolPrompt = "Write a poem about" )
    object SongLyrics : ToolItem(title = "Song Lyrics", subTitle = "Create a catchy tune.", toolPrompt = "Create lyrics for a song about" )
    object FoodRecipe : ToolItem(title = "Food Recipe", subTitle = "Cook up something delicious.", toolPrompt = " Invent a recipe for a dish that" )


    //Grammar
    object GrammerCorrection : ToolItem(title = "Grammer Correction", subTitle = "Let's fix your grammar.", toolPrompt = "Rewrite the following sentence with proper grammar." )
    object AnswerQuestion : ToolItem(title = "Answer to Question", subTitle = "I'll answer that for you.", toolPrompt = "Provide a response to the given question" )
    object ActivePassive : ToolItem(title = "Active to Passive", subTitle = "Let's make it passive.", toolPrompt = "Transform the following active voice sentence into passive voice." )
    object PassiveActive : ToolItem(title = "Passive to Active", subTitle = "Let's make it active.", toolPrompt = "Rewrite the following passive voice sentence into active voice." )


    //Job Essentials
    object JobDescription : ToolItem(title = "Job Description", subTitle = "Find the perfect fit for your team.", toolPrompt = "Write a job description" )
    object Resume : ToolItem(title = "Resume", subTitle = "Showcase your skills and experience.", toolPrompt = "Create a resume for" )
    object InterviewQuestions : ToolItem(title = "Interview Questions", subTitle = "Prepare for your next interview.", toolPrompt = "Develop a list of interview questions for a candidate" )
}