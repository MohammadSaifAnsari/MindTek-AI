package com.mohdsaifansari.mindtek.AdsMob

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoinViewModal : ViewModel() {
    private val _isCoin = MutableStateFlow<Int>(0)
    val isCoin: StateFlow<Int> = _isCoin.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val uid = firebaseAuth.currentUser?.uid.toString()

    init {
        getCoin()
    }

    fun getCoin() {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Coins").document(uid).get().addOnSuccessListener { task ->
                if (task != null) {
                    _isCoin.value = task.get("Total Coins").toString().toInt()
                } else {
                    _isCoin.value = 0
                    Log.d("CoinViewModal", "Error in fetching coins ")
                }
            }.addOnFailureListener { exception ->
                Log.d("CoinViewModal", "get failed with ", exception)
            }
        }
    }

    fun addCoin(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Coins").document(uid)
                .update("Total Coins", FieldValue.increment(10)).addOnSuccessListener {
                Toast.makeText(context, "Coins Added", Toast.LENGTH_SHORT).show();
            }.addOnFailureListener {
                Log.d("CoinViewModal", "Error in adding coins ")
            }
        }
    }

    fun subtractCoin(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Coins").document(uid)
                .update("Total Coins", FieldValue.increment(-10)).addOnSuccessListener {
                Toast.makeText(context, "10 Coins Used", Toast.LENGTH_SHORT).show();
            }.addOnFailureListener {
                Log.d("CoinViewModal", "Error in subtract coins ")
            }
        }
    }
}