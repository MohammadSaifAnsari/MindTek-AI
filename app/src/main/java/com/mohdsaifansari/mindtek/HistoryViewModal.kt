package com.mohdsaifansari.mindtek

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mohdsaifansari.mindtek.ui.theme.Data.LoadHistory
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    var historyData: MutableList<LoadHistory> =
        mutableStateListOf(LoadHistory(null, null, null, null, null, null))

    val firestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val uid = firebaseAuth.currentUser?.uid.toString()


    fun loadData(): MutableList<LoadHistory> {

        viewModelScope.launch {
            firestore.collection("History").document(uid).collection("ToolHistory")
                .orderBy("idNo", Query.Direction.DESCENDING).get()
                .addOnSuccessListener { documentSnapshot ->
                    historyData.clear()
                    for (document in documentSnapshot) {
                        val idNo = document.data.get("idNo")
                        val prompt = document.data.get("Prompt")
                        val result = document.data.get("Result")
                        val day = document.data.get("DayOfMonth")
                        val month = document.data.get("Month")
                        val year = document.data.get("Year")

                        historyData.add(
                            LoadHistory(
                                idNo.toString().toInt(), prompt.toString(), result.toString(),
                                day.toString(), month.toString(), year.toString()
                            )
                        )
                    }
                }
        }

        return historyData
    }


    fun deleteHistoryItem(idNum: Int, context: Context) {
        viewModelScope.launch {
            firestore.collection("History").document(uid).collection("ToolHistory")
                .whereEqualTo("idNo", idNum).get().addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot) {
                        val documentId = document.id
                        firestore.collection("History").document(uid)
                            .collection("ToolHistory").document(documentId).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
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