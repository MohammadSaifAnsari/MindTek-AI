package com.mohdsaifansari.mindtek.Authentication

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mohdsaifansari.mindtek.Components.LogInItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    val firestore = FirebaseFirestore.getInstance()

    fun signIn(
        auth: FirebaseAuth,
        email: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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
                } else {
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
                        }.addOnFailureListener {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show();
                        }

                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}