package com.mohdsaifansari.mindtek.History

import android.content.Context
import android.util.Log
import android.widget.Toast
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

class HistoryViewModel : ViewModel() {

    private val _historyData = MutableStateFlow<List<LoadHistory>>(emptyList())
    val historyData: StateFlow<List<LoadHistory>> = _historyData.asStateFlow()

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
                        }


                    }

                }
        }
    }
}