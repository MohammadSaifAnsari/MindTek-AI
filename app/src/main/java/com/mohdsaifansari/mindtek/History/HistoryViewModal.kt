package com.mohdsaifansari.mindtek.History

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mohdsaifansari.mindtek.Data.LoadHistory
import com.mohdsaifansari.mindtek.Database.ToolHistory.ToolHistoryDatabase
import com.mohdsaifansari.mindtek.Database.ToolHistory.ToolHistoryEntity
import com.mohdsaifansari.mindtek.Network.NetworkConnectivityChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class HistoryViewModel : ViewModel() {

    private val _historyData = MutableStateFlow<List<LoadHistory>>(emptyList())
    val historyData: StateFlow<List<LoadHistory>> = _historyData.asStateFlow()

    private val _isloading = MutableStateFlow<Boolean>(true)
    val isloading: StateFlow<Boolean> = _isloading.asStateFlow()

    val firestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val uid = firebaseAuth.currentUser?.uid.toString()
    private var isFetchingOnlineData = false


    fun deleteHistoryItem(idNum: Int, context: Context, db: ToolHistoryDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("History").document(uid).collection("ToolHistory")
                .whereEqualTo("idNo", idNum).get().addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot) {
                        val documentId = document.id
                        firestore.collection("History").document(uid)
                            .collection("ToolHistory").document(documentId).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                viewModelScope.launch(Dispatchers.IO) {
                                    db.toolHistoryDao().deleteToolHistoryById(idNum)
                                    checkToolHistoryData(context, db)
                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                    .show();
                            }
                    }

                }.addOnFailureListener {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
        }

    }

    suspend fun checkToolHistoryData(context: Context, db: ToolHistoryDatabase) {
        val networkConnectivityChecker = NetworkConnectivityChecker(context)
        viewModelScope.launch(Dispatchers.IO) {
            fetchOfflineToolHistoryData(db)
            networkConnectivityChecker.isNetworkAvailable().collect { isAvailable ->
                Log.d("ViewModel123", "Network available: $isAvailable")
                if (isAvailable && !isFetchingOnlineData) {
                    isFetchingOnlineData = true
                    fetchOnlineToolHistoryData(db)
                }
            }
        }
    }

    private fun fetchOfflineToolHistoryData(db: ToolHistoryDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            db.toolHistoryDao().getAllToolHistories().collectLatest { entities ->
                val updatedData = entities.reversed().map { entity ->
                    LoadHistory(
                        entity.idNo,
                        entity.prompt,
                        entity.result,
                        entity.day,
                        entity.month,
                        entity.year
                    )
                }
                _historyData.value = updatedData
            }
        }
    }

    private fun fetchOnlineToolHistoryData(db: ToolHistoryDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            _isloading.value = true
            firestore.collection("History").document(uid).collection("ToolHistory")
                .orderBy("idNo", Query.Direction.DESCENDING).get()
                .addOnSuccessListener { documentSnapshot ->
                    viewModelScope.launch(Dispatchers.IO) {
                        db.toolHistoryDao().deleteAllToolHistories()
                    }
                    for (document in documentSnapshot) {
                        val idNo = document.data.get("idNo")
                        val prompt = document.data.get("Prompt")
                        val result = document.data.get("Result")
                        val day = document.data.get("DayOfMonth")
                        val month = document.data.get("Month")
                        val year = document.data.get("Year")

                        viewModelScope.launch(Dispatchers.IO) {
                            db.toolHistoryDao().upsertToolHistory(
                                ToolHistoryEntity(
                                    idNo.toString().toInt(),
                                    prompt.toString(),
                                    result.toString(),
                                    day.toString(),
                                    month.toString(),
                                    year.toString()
                                )
                            )
                            _isloading.value = false
                        }
                    }
                }.addOnFailureListener {
                    _isloading.value = false
                    Log.d("ViewModel123", "Error fetching data: ${it.message}")
                }
        }
    }

    fun saveTextasPdf(
        text: String, fileName: String, pageWidth: Int = 794,
        pageHeight: Int = 1123,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pdfDocument = PdfDocument()
                var currentPage = pdfDocument.startPage(
                    PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
                )

                val paint = Paint()
                paint.color = Color.Black.toArgb()
                paint.textSize = 25f

                val x = 20f
                var y = 50f
                var currentLine = ""

                for (word in text.split(" ")) {
                    var potentialLine = if (currentLine.isEmpty()) {
                        word
                    } else {
                        "$currentLine $word"
                    }
                    // Calculate the width of the remaining text
                    val lineWidth = paint.measureText(potentialLine)

                    // Check if the text fits on the current line
                    if ((lineWidth <= pageWidth - 2 * x) && (y + paint.descent() - paint.ascent() <= pageHeight - 50f)) {
                        // Word fits on current line and page
                        currentLine = potentialLine
                    } else {

                        //Draw the current line
                        currentPage.canvas.drawText(currentLine.trim(), x, y, paint)

                        //Move to the next line or page
                        y += paint.descent() - paint.ascent()
                        if (y + paint.descent() - paint.ascent() > pageHeight - 50f) {
                            pdfDocument.finishPage(currentPage)
                            currentPage = pdfDocument.startPage(
                                PdfDocument.PageInfo.Builder(
                                    pageWidth,
                                    pageHeight,
                                    pdfDocument.pages.size + 1
                                ).create()
                            )
                            y = 50f
                        }
                        // Start a new line with the current word
                        currentLine = word
                    }
                }
                // Draw the last remaining line
                if (currentLine.isNotEmpty()) {
                    currentPage.canvas.drawText(currentLine.trim(), x, y, paint)
                }
                pdfDocument.finishPage(currentPage)

                val mindtekDirectory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "Mindtek"
                )
                if (!mindtekDirectory.exists()) {
                    mindtekDirectory.mkdirs()
                }
                var filePath = File(mindtekDirectory, "$fileName.pdf")

                //Handle duplicate file names
                var fileNumber = 1
                while (filePath.exists()) {
                    filePath = File(mindtekDirectory, "${fileName} (${fileNumber}).pdf")
                    fileNumber++
                }

                val fileOutputStream = FileOutputStream(filePath)
                pdfDocument.writeTo(fileOutputStream)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                }
                pdfDocument.close()
                fileOutputStream.close()
                Log.d("his123", "saved")
            } catch (e: Exception) {
                Log.d("his123", "not saved")
                e.printStackTrace()
            }
        }
    }
}