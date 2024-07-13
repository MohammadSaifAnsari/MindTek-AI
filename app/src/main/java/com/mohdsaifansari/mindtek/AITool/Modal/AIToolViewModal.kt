package com.mohdsaifansari.mindtek.AITool.Modal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mohdsaifansari.mindtek.AITool.Data.ToolItem
import com.mohdsaifansari.mindtek.Apikey
import com.mohdsaifansari.mindtek.Data.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class AIToolViewModal : ViewModel() {

    private val _isloadingAnimation = MutableStateFlow<Boolean>(true)
    val isloadingAnimation: StateFlow<Boolean> = _isloadingAnimation.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val uid = firebaseAuth.currentUser?.uid.toString()

    val list by lazy {
        mutableStateListOf<String>()
    }
    private val genAi by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = Apikey

        )
    }

    fun sendMessage(message: String) = viewModelScope.launch() {
        var fullResponse = ""
        genAi.generateContentStream(message).collect() { chunk ->
            list.add(chunk.text.toString())
            fullResponse += chunk.text
            _isloadingAnimation.value = false
        }

    }

    suspend fun splitPdfIntoImages(getText:(String) -> Unit, path: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            var extractText = ""
            try {
                val parcelFileDescriptor: ParcelFileDescriptor? =
                    context.contentResolver.openFileDescriptor(Uri.parse(path), "r")
                val render = parcelFileDescriptor?.let { PdfRenderer(it) }
                if (render != null) {
                    for (i in 0 until render.pageCount) {
                        val page = render.openPage(i)
                        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        val inputImage = InputImage.fromBitmap(bitmap, 0)
                        val visionText = recognizer.process(inputImage).await()
                        extractText += visionText.text
                        page.close()
                    }
                    getText(extractText)
                    render.close()
                    parcelFileDescriptor.close()
                    Log.d("ExtractVisionText", extractText)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun savedData(prompt: String, result: String, title: String, context: Context) {
        val date = date()
        firestore.collection("History").document(uid).get()
            .addOnSuccessListener { documentSnapshot ->
                val count = documentSnapshot.getLong("Count")
                val data = hashMapOf(
                    "idNo" to count,
                    "toolTitle" to title,
                    "Prompt" to prompt,
                    "Result" to result,
                    "DayOfMonth" to date.dayofMonth,
                    "Month" to date.month,
                    "Year" to date.year
                )
                firestore.collection("History").document(uid).collection("ToolHistory").add(data)
                    .addOnFailureListener {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
            }.addOnFailureListener {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun date(): Date {
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.month
        val dayOfMonth = currentDate.dayOfMonth
        return Date(
            dayOfMonth.toString(), month.toString(), year.toString()
        )

    }

    fun PromptCase(title: String): String {
        if (title == ToolItem.MailGeneration.title) {
            return ToolItem.MailGeneration.toolPrompt
        } else if (title == ToolItem.BlogGeneration.title) {
            return ToolItem.BlogGeneration.toolPrompt
        } else if (title == ToolItem.BlogSection.title) {
            return ToolItem.BlogSection.toolPrompt
        } else if (title == ToolItem.BlogIdeas.title) {
            return ToolItem.BlogIdeas.toolPrompt
        } else if (title == ToolItem.ParagraphGenerator.title) {
            return ToolItem.ParagraphGenerator.toolPrompt
        } else if (title == ToolItem.GenerateArticle.title) {
            return ToolItem.GenerateArticle.toolPrompt
        } else if (title == ToolItem.CreativeStory.title) {
            return ToolItem.CreativeStory.toolPrompt
        } else if (title == ToolItem.CreativeLetter.title) {
            return ToolItem.CreativeLetter.toolPrompt
        } else if (title == ToolItem.LoveLetter.title) {
            return ToolItem.LoveLetter.toolPrompt
        } else if (title == ToolItem.Poems.title) {
            return ToolItem.Poems.toolPrompt
        } else if (title == ToolItem.SongLyrics.title) {
            return ToolItem.SongLyrics.toolPrompt
        } else if (title == ToolItem.FoodRecipe.title) {
            return ToolItem.FoodRecipe.toolPrompt
        } else if (title == ToolItem.GrammerCorrection.title) {
            return ToolItem.GrammerCorrection.toolPrompt
        } else if (title == ToolItem.AnswerQuestion.title) {
            return ToolItem.AnswerQuestion.toolPrompt
        } else if (title == ToolItem.ActivePassive.title) {
            return ToolItem.ActivePassive.toolPrompt
        } else if (title == ToolItem.PassiveActive.title) {
            return ToolItem.PassiveActive.toolPrompt
        } else if (title == ToolItem.JobDescription.title) {
            return ToolItem.JobDescription.toolPrompt
        } else if (title == ToolItem.Resume.title) {
            return ToolItem.Resume.toolPrompt
        } else if (title == ToolItem.InterviewQuestions.title) {
            return ToolItem.InterviewQuestions.toolPrompt
        } else if (title == ToolItem.TextSummarizer.title) {
            return ToolItem.TextSummarizer.toolPrompt
        } else if (title == ToolItem.StorySummarizer.title) {
            return ToolItem.StorySummarizer.toolPrompt
        } else {
            return ToolItem.ParagraphSummarizer.toolPrompt
        }
    }
}