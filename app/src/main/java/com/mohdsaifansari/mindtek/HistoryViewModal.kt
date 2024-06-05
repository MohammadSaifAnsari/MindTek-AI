package com.mohdsaifansari.mindtek

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mohdsaifansari.mindtek.ui.theme.Data.loadHistory

class HistoryViewModel : ViewModel() {

    var historyData: MutableList<loadHistory> =
        mutableStateListOf(loadHistory(0, "", "", "", "", ""))

    fun loadData(): MutableList<loadHistory> {
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid.toString()
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
                        loadHistory(
                            idNo.toString().toInt(), prompt.toString(), result.toString(),
                            day.toString(), month.toString(), year.toString()
                        )
                    )
                }
            }
        return historyData
    }

}