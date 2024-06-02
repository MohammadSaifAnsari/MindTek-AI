package com.mohdsaifansari.mindtek.ui.theme

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mohdsaifansari.mindtek.ui.theme.Components.LogInItem
import com.mohdsaifansari.mindtek.ui.theme.Data.UserData


@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    navController: NavController,
    context: Context
) {
    val profileData = getUserData(firebaseAuth, firestore, context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )
        IconButton(
            onClick = {
            }, modifier = Modifier
                .padding(4.dp)
                .size(120.dp)
        ) {
            Icon(
                Icons.Default.Face, contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
        }
        //Name
        Text(
            text = profileData.firstName + profileData.lastName,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier.padding(5.dp)
        )
        //Email
        Text(
            text = profileData.email, fontWeight = FontWeight.Light, fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 15.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        //About Us
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
            Text(
                text = "About Us",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.8f))
            Icon(
                Icons.Default.KeyboardArrowRight, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        //Rate App
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Star, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
            Text(
                text = "Rate App",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.8f))
            Icon(
                Icons.Default.KeyboardArrowRight, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        //Share
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Share, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
            Text(
                text = "Share Mindtek",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.75f))
            Icon(
                Icons.Default.KeyboardArrowRight, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        //Privacy Policy
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Lock, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
            Text(
                text = "Privacy Policy",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.75f))
            Icon(
                Icons.Default.KeyboardArrowRight, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        //Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    firebaseAuth.signOut()
                    navController.navigate(LogInItem.AuthScreen.route) {
                        popUpTo(LogInItem.HomeScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
            Text(
                text = "Logout",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.80f))
            Icon(
                Icons.Default.KeyboardArrowRight, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                    .background(Color.Transparent), tint = Color.Black
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
    }
}

@Composable
fun getUserData(
    firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    context: Context
): UserData {
    var firstUserName by remember {
        mutableStateOf("")
    }
    var lastUserName by remember {
        mutableStateOf("")
    }
    var curUserEmail by remember {
        mutableStateOf("")
    }

    firestore.collection("Users").document(firebaseAuth.currentUser?.uid.toString()).get()
        .addOnSuccessListener { documentSnapshot ->
            firstUserName = documentSnapshot.getString("First Name").toString();
            lastUserName = documentSnapshot.getString("Last Name").toString();
            curUserEmail = documentSnapshot.getString("Email").toString();
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    Log.d("qwe123", UserData(firstUserName, lastUserName, curUserEmail).toString())
    return UserData(firstUserName, lastUserName, curUserEmail)
}