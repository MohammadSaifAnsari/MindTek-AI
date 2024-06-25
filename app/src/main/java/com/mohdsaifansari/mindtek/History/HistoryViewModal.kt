package com.mohdsaifansari.mindtek.History

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mohdsaifansari.mindtek.Data.LoadHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _historyData = MutableStateFlow<List<LoadHistory>>(emptyList())
    val historyData: StateFlow<List<LoadHistory>> = _historyData.asStateFlow()

    val firestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val uid = firebaseAuth.currentUser?.uid.toString()

    init {
        loadData()
    }

    fun loadData() {

        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("History").document(uid).collection("ToolHistory")
                .orderBy("idNo", Query.Direction.DESCENDING).get()
                .addOnSuccessListener { documentSnapshot ->
                    val updatedData = mutableListOf<LoadHistory>()
                    for (document in documentSnapshot) {
                        val idNo = document.data.get("idNo")
                        val prompt = document.data.get("Prompt")
                        val result = document.data.get("Result")
                        val day = document.data.get("DayOfMonth")
                        val month = document.data.get("Month")
                        val year = document.data.get("Year")

                        updatedData.add(
                            LoadHistory(
                                idNo.toString().toInt(), prompt.toString(), result.toString(),
                                day.toString(), month.toString(), year.toString()
                            )
                        )
                    }
                    _historyData.value = updatedData
                }
        }

    }


    fun deleteHistoryItem(idNum: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("History").document(uid).collection("ToolHistory")
                .whereEqualTo("idNo", idNum).get().addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot) {
                        val documentId = document.id
                        firestore.collection("History").document(uid)
                            .collection("ToolHistory").document(documentId).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                loadData()
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

}