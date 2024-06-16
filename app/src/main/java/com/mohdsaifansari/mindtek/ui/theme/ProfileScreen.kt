package com.mohdsaifansari.mindtek.ui.theme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mohdsaifansari.mindtek.R
import com.mohdsaifansari.mindtek.ui.theme.Components.LogInItem
import com.mohdsaifansari.mindtek.ui.theme.Data.ProfileItem
import com.mohdsaifansari.mindtek.ui.theme.Data.ProfilePhotoKey
import com.mohdsaifansari.mindtek.ui.theme.Data.UserData
import okio.IOException
import java.io.File


@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    navController: NavController,
    context: Context
) {
    val profileData = getUserData(firebaseAuth, firestore, context)
    val profileItemlist = listOf(
        "Personal info",
        "Help Center",
        "Privacy Policy",
        "About Mindtek",
        "Logout"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFDCE2F1), // #dee4f4
                        Color(0xFFFFFFFF)
                    ), start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            ),
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
                .clip(CircleShape)
        ) {
            ProfilePicture(
                context = context,
                profileKey = ProfilePhotoKey.ProfileScreenPhotoKey.name
            )
        }
        //Name
        Text(
            text = profileData.firstName + profileData.lastName,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier.padding(5.dp),
            fontFamily = FontFamily.Serif
        )
        //Email
        Text(
            text = profileData.email, fontWeight = FontWeight.Light, fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 14.dp),
            fontFamily = FontFamily.SansSerif
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        ProfileItemRow(navController, firebaseAuth = firebaseAuth, items = profileItemlist)
    }
}


@Composable
fun ProfileItemRow(navController: NavController, firebaseAuth: FirebaseAuth, items: List<String>) {
    for (item in items) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    if (item == "Logout") {
                        firebaseAuth.signOut()
                        navController.navigate(LogInItem.AuthScreen.route) {
                            popUpTo(LogInItem.HomeScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else if (item == "Personal info") {
                        navController.navigate(LogInItem.ProfileEditNav.route) {
                            popUpTo(LogInItem.ProfileEditNav.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = profileItemIcon(item)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp),
                    if (item == "Logout") {
                        colorResource(id = R.color.red)
                    } else {
                        colorResource(id = R.color.black)
                    }
                )
                Text(
                    text = item,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp, fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(5.dp),
                    color = if (item == "Logout") {
                        Color.Red
                    } else {
                        Color.Black
                    }
                )
            }
            if (item != "Logout") {
                Icon(
                    Icons.Default.KeyboardArrowRight, contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp)
                        .background(Color.Transparent), tint = Color.Black
                )
            }
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
    )
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
    return UserData(firstUserName, lastUserName, curUserEmail)
}

@Composable
fun ProfilePicture(context: Context, profileKey: String) {
    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        retrieveProfilePicture(context) { bitmap ->
            bitmapState = bitmap
        }
    }
    if (profileKey == ProfilePhotoKey.ProfileScreenPhotoKey.name) {
        if (bitmapState != null) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 2.dp)
                    .clip(CircleShape),
                contentDescription = "picked profile image",
                contentScale = ContentScale.Crop,
                bitmap = bitmapState!!.asImageBitmap()
            )
        } else {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 2.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
                painter = painterResource(id = R.drawable.personicon),
                contentDescription = null
            )
        }
    } else if (profileKey == ProfilePhotoKey.NavDrawerPhotoKey.name) {
        if (bitmapState != null) {
            Row {
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 2.dp)
                        .clip(CircleShape),
                    contentDescription = "picked profile image",
                    contentScale = ContentScale.Crop,
                    bitmap = bitmapState!!.asImageBitmap()
                )
            }

        } else {
            Row {
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 2.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit,
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            }

        }
    }


}

fun retrieveProfilePicture(context: Context, onBitmapRetrieved: (Bitmap?) -> Unit) {
    val firebaseStorage = FirebaseStorage.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    val uid = firebaseAuth.currentUser?.uid.toString()
    val storageReference = firebaseStorage.getReference()
    val profilePhoto = storageReference.child("Users/$uid/Profile Photo")

    try {
        val file = File.createTempFile("Profile Photo", ".png")
        profilePhoto.getFile(file).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            onBitmapRetrieved(bitmap)
            Log.d("photo123", "run")
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            onBitmapRetrieved(null)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        onBitmapRetrieved(null)
    }
}


fun profileItemIcon(itemname: String): Int {
    if (itemname == ProfileItem.Personalinfo.name) {
        return ProfileItem.Personalinfo.painter
    } else if (itemname == ProfileItem.HelpCenter.name) {
        return ProfileItem.HelpCenter.painter
    } else if (itemname == ProfileItem.PrivacyPolicy.name) {
        return ProfileItem.PrivacyPolicy.painter
    } else if (itemname == ProfileItem.AboutMindtek.name) {
        return ProfileItem.AboutMindtek.painter
    } else {
        return ProfileItem.Logout.painter
    }
}