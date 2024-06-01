package com.mohdsaifansari.mindtek

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mohdsaifansari.mindtek.ui.theme.Components.LogInItem


@Composable
fun SignUpScreen(auth: FirebaseAuth, context: Context, navController: NavController) {
    var firstNametext by remember {
        mutableStateOf("")
    }
    var lastNametext by remember {
        mutableStateOf("")
    }
    var emailtext by remember {
        mutableStateOf("")
    }
    var passwordtext by remember {
        mutableStateOf("")
    }
    val isErrorinEmail by remember {
        derivedStateOf {
            if (emailtext.isEmpty()) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(emailtext).matches().not()
            }
        }
    }
    var isEnabledButton by remember {
        mutableStateOf(false)
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register",
                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                fontSize = 25.sp, fontWeight = FontWeight.Bold
            )
            Text(
                text = "Join the AI revolution with the Mindtek",
                modifier = Modifier.padding(top = 4.dp, bottom = 30.dp),
                fontSize = 14.sp
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                value = firstNametext,
                onValueChange = { newText ->
                    firstNametext = newText
                },
                placeholder = {
                    Text(text = "First Name")
                },
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(text = "First Name")
                }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                value = lastNametext,
                onValueChange = { newText ->
                    lastNametext = newText
                },
                placeholder = {
                    Text(text = "Last Name")
                },
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(text = "Last Name")
                }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                value = emailtext,
                onValueChange = { newText ->
                    emailtext = newText
                },
                placeholder = {
                    Text(text = "Email")
                },
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(text = "Email")
                },
                supportingText = {
                    if (isErrorinEmail) {
                        Text(text = "Enter a valid email address")
                    }
                }, isError = isErrorinEmail
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                value = passwordtext,
                onValueChange = { newText ->
                    passwordtext = newText
                },
                placeholder = {
                    Text(text = "Password")
                },
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(text = "Password")
                }
            )
            if (firstNametext.isNotEmpty() && lastNametext.isNotEmpty() && passwordtext.isNotEmpty() && emailtext.isNotEmpty() && (isErrorinEmail == false)) {
                isEnabledButton = true
            }

            Button(
                onClick = {
                    signUp(
                        auth = auth,
                        firstName = firstNametext,
                        lastName = lastNametext,
                        email = emailtext,
                        password = passwordtext,
                        context = context,
                        navController = navController
                    )
                },
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(0.5f),
                enabled = isEnabledButton
            ) {
                Text(text = "Sign Up")
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
            Text(
                text = "-Or sign up with-",
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Email, contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                        .background(Color.Transparent), tint = Color.Black
                )

                Icon(
                    Icons.Default.KeyboardArrowRight, contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                        .background(Color.Transparent), tint = Color.Black
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
                ClickableText(text = AnnotatedString(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Red, fontWeight = FontWeight.Normal, fontSize = 14.sp
                        )
                    ) {
                        append("Login")
                    }
                }.toString()), onClick = {
                    navController.navigate(LogInItem.AuthScreen.route) {
                        popUpTo(LogInItem.AuthScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, modifier = Modifier.padding(end = 5.dp))
            }
        }
        isEnabledButton = false
    }
}

private fun signUp(
    auth: FirebaseAuth,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    context: Context,
    navController: NavController
) {
    val firestore = FirebaseFirestore.getInstance()
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {

            //Create a userPofile in firestore
            val userProfile = hashMapOf(
                "First Name" to firstName,
                "Last Name" to lastName,
                "Email" to email,
                "Password" to password
            )
            val uid = task.getResult().user?.uid
            firestore.collection("Users").document(uid.toString()).set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    navController.navigate(LogInItem.HomeScreen.route) {
                        popUpTo(LogInItem.AuthScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}

