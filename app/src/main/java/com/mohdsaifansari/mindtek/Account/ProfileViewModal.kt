package com.mohdsaifansari.mindtek.Account

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mohdsaifansari.mindtek.Database.User
import com.mohdsaifansari.mindtek.Database.UserDatabase
import com.mohdsaifansari.mindtek.Network.NetworkConnectivityChecker
import com.mohdsaifansari.mindtek.Data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class ProfileViewModel() : ViewModel() {
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private val _profilePictureBitmap = MutableStateFlow<Bitmap?>(null)
    val profilePictureBitmap: StateFlow<Bitmap?> = _profilePictureBitmap.asStateFlow()


    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val uid = firebaseAuth.currentUser?.uid.toString()

    private var isFetchingOnlineData = false

    fun checkUserData(context: Context, db: UserDatabase) {
        val networkConnectivityChecker = NetworkConnectivityChecker(context)
        viewModelScope.launch(Dispatchers.IO) {
            fetchOfflineData(db)
            networkConnectivityChecker.isNetworkAvailable().collect { isAvailable ->
                Log.d("ViewModel123", "Network available: $isAvailable")
                if (isAvailable && !isFetchingOnlineData) {
                    isFetchingOnlineData = true // Set the flag to indicate fetching
                    fetchOnlineData(context, db)
                }
            }
        }
    }

    private fun fetchOnlineData(context: Context, db: UserDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fetchUserData(context, db)
                fetchProfilePicture(context)

            } catch (e: Exception) {
                Log.e("prof123", "Error fetching online data: ${e.message}")
                // Consider showing an error message to the user
            }
        }
    }

    private fun fetchOfflineData(db: UserDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val latestUser = db.dao.getLatestUser()
                _userData.value = latestUser?.let {
                    UserData(it.firstName, it.lastName, it.email)
                }
                fetchOfflineProfilePicture(db)
                if (_userData.value != null) {
                    Log.d("prof123", "Offline data loaded")
                } else {
                    Log.d("prof123", "Database is empty, no offline data")
                }
            } catch (e: Exception) {
                Log.e("prof123", "Error fetching offline data: ${e.message}", e)
            }
        }
    }

    private fun fetchOfflineProfilePicture(db: UserDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val latestUser = db.dao.getLatestUser()
                _profilePictureBitmap.value = latestUser?.profilePicture?.let { byteArray ->
                    byteArrayToBitmap(byteArray)
                }
            } catch (e: Exception) {
                Log.e("prof123", "Error fetching offline profile picture data: ${e.message}", e)
            }
        }
    }


    fun fetchUserData(context: Context, db: UserDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Users").document(firebaseAuth.currentUser?.uid.toString()).get()
                .addOnSuccessListener { documentSnapshot ->
                    val firstUserName = documentSnapshot.getString("First Name").toString()
                    val lastUserName = documentSnapshot.getString("Last Name").toString()
                    val curUserEmail = documentSnapshot.getString("Email").toString()
                    _userData.value = UserData(firstUserName, lastUserName, curUserEmail)
                    viewModelScope.launch {
                        db.dao.upsertUser(User(1, firstUserName, lastUserName, curUserEmail))
                        retrieveProfilePicture(context) { bitmap ->
                            try {
                                viewModelScope.launch {
                                    val byteArray = bitmap?.toByteArray()
                                    db.dao.upsertUser(
                                        User(
                                            1,
                                            firstUserName,
                                            lastUserName,
                                            curUserEmail,
                                            byteArray
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("prof123", "Error fetching online data: ${e.message}")
                            }

                        }

                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
        }
    }

    suspend fun logoutClearDatabase(db: UserDatabase) {
        db.dao.deleteAllUser()
    }

    private fun fetchProfilePicture(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            retrieveProfilePicture(context) { bitmap ->
                _profilePictureBitmap.value = bitmap
            }
        }
    }

    private fun retrieveProfilePicture(context: Context, onBitmapRetrieved: (Bitmap?) -> Unit) {


        val storageReference = firebaseStorage.getReference()
        val profilePhoto = storageReference.child("Users/$uid/Profile Photo")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val timestamp = System.currentTimeMillis()
                val file = File.createTempFile("Profile Photo_$timestamp", ".png")
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

    }

    fun updateProfileData(
        firstName: String,
        lastName: String,
        context: Context,
        db: UserDatabase
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val uid = firebaseAuth.currentUser?.uid.toString()

            val userProfile = hashMapOf<String, Any>(
                "First Name" to firstName,
                "Last Name" to lastName
            )
            firestore.collection("Users").document(uid)
                .update(userProfile)
                .addOnSuccessListener {
                    fetchUserData(context, db)
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Something went wrong while updating name",
                        Toast.LENGTH_SHORT
                    ).show();
                }
        }

    }

    fun uploadProfilePicture(context: Context, uri: Uri, db: UserDatabase) {
        val storageReference = firebaseStorage.getReference()
        val profilePhoto = storageReference.child("Users/$uid/Profile Photo")

        viewModelScope.launch(Dispatchers.IO) {
            profilePhoto.putFile(uri).addOnSuccessListener {
                fetchUserData(context, db)
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Something went wrong while uploading profile photo",
                    Toast.LENGTH_SHORT
                ).show();
            }
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

    private fun Bitmap.toByteArray():
            ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 50, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}