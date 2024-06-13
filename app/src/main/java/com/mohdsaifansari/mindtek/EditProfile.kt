package com.mohdsaifansari.mindtek

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mohdsaifansari.mindtek.ui.theme.ProfilePicture
import com.mohdsaifansari.mindtek.ui.theme.getUserData
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun EditProfile(
    firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    context: Context,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    uriState: MutableStateFlow<String>
) {
    Scaffold(topBar = {
        EditProfileHeader()
    }) { innerPadding ->
        MainContentEditProfile(
            innerPadding,
            firebaseAuth,
            firestore,
            context,
            imagePicker,
            uriState
        )
    }
}


@Composable
fun MainContentEditProfile(
    paddingValues: PaddingValues,
    firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    context: Context,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    uriState: MutableStateFlow<String>
) {
    val bitmap = getProfileBitmap(uriState = uriState)
    val profileData = getUserData(firebaseAuth, firestore, context)
    var firstNametext by remember {
        mutableStateOf("")
    }
    var lastNametext by remember {
        mutableStateOf("")
    }

    if ((firstNametext == "") && (lastNametext == "")) {
        firstNametext = profileData.firstName
        lastNametext = profileData.lastName
    }
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
                imagePicker.launch(
                    PickVisualMediaRequest
                        .Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        .build()
                )
            }, modifier = Modifier
                .padding(4.dp)
                .size(120.dp)
                .clip(CircleShape)
        ) {
            bitmap.let {
                if (it != null) {
                    Image(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 2.dp)
                            .clip(CircleShape),
                        contentDescription = "picked profile image",
                        contentScale = ContentScale.Crop,
                        bitmap = it.asImageBitmap()
                    )
                } else {
                    ProfilePicture(context = context)
                }
            }

        }
        Text(
            text = "Change Picture",
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            modifier = Modifier.padding(5.dp),
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(25.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        )
        {
            Text(
                text = "First Name",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(3.dp),
                fontFamily = FontFamily.Serif
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = firstNametext,
                onValueChange = { newText ->
                    firstNametext = newText
                },
                placeholder = {
                    Text(text = "First Name")
                },
                shape = RoundedCornerShape(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        )
        {
            Text(
                text = "Last Name",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(3.dp),
                fontFamily = FontFamily.Serif
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = lastNametext,
                onValueChange = { newText ->
                    lastNametext = newText
                },
                placeholder = {
                    Text(text = "Last Name")
                },
                shape = RoundedCornerShape(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        val uri = uriState.collectAsState().value.toUri()
        Button(
            onClick = {
                updateProfileData(firestore, firebaseAuth, firstNametext, lastNametext, context)
                uploadProfilePicture(context = context, uri = uri)
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "Update",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(2.dp),
                color = Color.White
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileHeader() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Edit Profile",
                modifier = Modifier.padding(5.dp),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
        },
        colors = TopAppBarColors(
            containerColor = Color(220, 226, 241, 255),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            scrolledContainerColor = Color.White
        ), navigationIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                modifier = Modifier.padding(5.dp),
                contentDescription = null
            )
        }
    )
}

@Composable
fun getProfileBitmap(uriState: MutableStateFlow<String>): Bitmap? {
    val uri = uriState.collectAsState().value

    val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(uri).size(Size.ORIGINAL).build()
    ).state

    if (imageState is AsyncImagePainter.State.Success) {
        return imageState.result.drawable.toBitmap()
    }
    return null
}

fun updateProfileData(
    firestore: FirebaseFirestore,
    firebaseAuth: FirebaseAuth,
    firstName: String,
    lastName: String,
    context: Context
) {
    val uid = firebaseAuth.currentUser?.uid.toString()

    val userProfile = hashMapOf<String, Any>(
        "First Name" to firstName,
        "Last Name" to lastName
    )
    firestore.collection("Users").document(uid)
        .update(userProfile)
        .addOnSuccessListener {
        }.addOnFailureListener {
            Toast.makeText(
                context,
                "Something went wrong while updating name",
                Toast.LENGTH_SHORT
            ).show();
        }
}

fun uploadProfilePicture(context: Context, uri: Uri) {
    val firebaseStorage = FirebaseStorage.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    val uid = firebaseAuth.currentUser?.uid.toString()
    var storageReference = firebaseStorage.getReference()
    val profilePhoto = storageReference.child("Users/$uid/Profile Photo")

    profilePhoto.putFile(uri).addOnSuccessListener {
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
    }.addOnFailureListener {
        Toast.makeText(
            context,
            "Something went wrong while uploading profile photo",
            Toast.LENGTH_SHORT
        ).show();
    }
}