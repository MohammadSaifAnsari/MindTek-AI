package com.mohdsaifansari.mindtek.Authentication

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mohdsaifansari.mindtek.Components.LogInItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {

    val firestore = FirebaseFirestore.getInstance()

    private val _isloadingAnimation = MutableStateFlow<Boolean>(false)
    val isloadingAnimation: StateFlow<Boolean> = _isloadingAnimation.asStateFlow()

    fun signIn(
        auth: FirebaseAuth,
        email: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _isloadingAnimation.value = true
            }
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                    navController.navigate(LogInItem.HomeScreen.route) {
                        popUpTo(LogInItem.AuthScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    _isloadingAnimation.value = false
                } else {
                    _isloadingAnimation.value = false
                    Toast.makeText(
                        context,
                        task.getException()?.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show();
                }
            }
        }

    }

    fun signUp(
        auth: FirebaseAuth,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _isloadingAnimation.value = true
            }
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //Create a userPofile in firestore
                    val userProfile = hashMapOf(
                        "First Name" to firstName,
                        "Last Name" to lastName,
                        "Email" to email,
                        "Password" to password
                    )
                    val count = hashMapOf(
                        "Count" to 0
                    )
                    val uid = task.getResult().user?.uid
                    firestore.collection("Users").document(uid.toString()).set(userProfile)
                        .addOnSuccessListener {
                            firestore.collection("History").document(uid.toString()).set(count)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Sign Up Successful",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show();
                                    navController.navigate(LogInItem.HomeScreen.route) {
                                        popUpTo(LogInItem.AuthScreen.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }
                            _isloadingAnimation.value = false
                        }.addOnFailureListener {
                            _isloadingAnimation.value = false
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show();
                        }

                } else {
                    _isloadingAnimation.value = false
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    fun closeKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, 0)
    }
}