package com.mohdsaifansari.mindtek.ui.theme.AITool.Data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import com.mohdsaifansari.mindtek.ui.theme.Components.BottomNavItem

sealed class ToolItem(val title:String,val subTitle:String,val toolPrompt:String) {
    object MailGeneration : ToolItem(title = "Mail Generation", subTitle = "Write a professional email in a few seconds.", toolPrompt = " Please draft a email on")
    object BlogGeneration : ToolItem(title = "Blog Generation", subTitle = "Create a compelling blog post in minutes.", toolPrompt = " Write a blog post on the topic")
    object BlogSection : ToolItem(title = "Blog Section", subTitle = "Generate a well-structured blog section on any topic.", toolPrompt = "Create a section for a blog post titled")
    object BlogIdeas : ToolItem(title = "Blog Ideas", subTitle = "Get inspired with unique and engaging blog post ideas.", toolPrompt = "Provide some blog post ideas for a ")
    object ParagraphGenerator : ToolItem(title = "Paragraph Generator", subTitle = "Write a high-quality paragraph on any topic.", toolPrompt = "Generate a paragraph explaining the")
    object GenerateArticle : ToolItem(title = "Generate Article", subTitle = "Create a comprehensive article on any topic.", toolPrompt = "Write a detailed article about the")


}