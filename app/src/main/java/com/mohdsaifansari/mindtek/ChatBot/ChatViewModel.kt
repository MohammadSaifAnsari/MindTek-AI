package com.mohdsaifansari.mindtek.ChatBot

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mohdsaifansari.mindtek.Database.ChatDatabase
import com.mohdsaifansari.mindtek.Database.ChatEntity
import com.mohdsaifansari.mindtek.Network.NetworkConnectivityChecker
import com.mohdsaifansari.mindtek.ChatBot.Data.ChatData
import com.mohdsaifansari.mindtek.ChatBot.Data.ChatState
import com.mohdsaifansari.mindtek.ChatBot.Data.ChatWithUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class ChatViewModel : ViewModel() {
    private val _chatstate = MutableStateFlow(ChatState())
    val chatstate = _chatstate.asStateFlow()

    private val _isloadingAnimation = MutableStateFlow<Boolean>(false)
    val isloadingAnimation: StateFlow<Boolean> = _isloadingAnimation.asStateFlow()

    private val _iscircularloading = MutableStateFlow<Boolean>(false)
    val iscircularloading: StateFlow<Boolean> = _iscircularloading.asStateFlow()

    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val uid = firebaseAuth.currentUser?.uid.toString()
    private var isFetchingOnlineData = false

    fun onEvent(event: ChatUiState) {
        when (event) {
            is ChatUiState.SendPrompt -> {
                if (event.prompt.isNotEmpty()) {
                    addPrompt(event.prompt, event.bitmap, event.imageUri)
                }
                if (event.bitmap != null) {
                    getResponseImage(event.prompt, event.bitmap)
                } else {
                    getResponse(event.prompt)
                }
            }

            is ChatUiState.UpdatePrompt -> {
                _chatstate.update {
                    it.copy(prompt = event.newPrompt)
                }
            }
        }
    }

    private fun addPrompt(prompt: String, bitmap: Bitmap?, imageUri: String) {
        _isloadingAnimation.value = true
        if (bitmap != null) {
            uploadTextWithImageData(prompt, bitmap.toByteArray(), true, System.currentTimeMillis())
        } else {
            uploadTextData(prompt, true, System.currentTimeMillis())
        }
        _chatstate.update {

            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, ChatWithUri(System.currentTimeMillis(), prompt, imageUri, true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }

    private fun getResponse(prompt: String) {
        viewModelScope.launch {
            val chat = ChatData.getResponse(prompt, chatstate.value.chatList)
            uploadTextData(chat.message, chat.isUser, System.currentTimeMillis())
            _chatstate.update {
                _isloadingAnimation.value = false
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    },
                )
            }
        }
    }

    private fun getResponseImage(prompt: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val chat = ChatData.getResponseImage(prompt, bitmap)
            uploadTextData(chat.message, chat.isUser, System.currentTimeMillis())
            _chatstate.update {
                _isloadingAnimation.value = false
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    },
                )
            }
        }
    }

    private fun uploadTextWithImageData(
        message: String,
        byteArray: ByteArray,
        isUser: Boolean,
        timestamp: Long
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val storageReference = firebaseStorage.getReference()
            val chatImage = storageReference.child("Users/$uid/ChatList/${UUID.randomUUID()}.png")
            val uploadTask = chatImage.putBytes(byteArray)
            uploadTask.addOnSuccessListener {
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    chatImage.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        val onlineChatList = hashMapOf<String, Any>(
                            "message" to message,
                            "imageAddress" to downloadUrl,
                            "isUser" to isUser,
                            "timestamp" to timestamp
                        )
                        firestore.collection("ChatBot")
                            .document(uid).collection("ChatList").add(onlineChatList)
                            .addOnSuccessListener {
                                Log.d("ChatViewModal", "Chat List data uploaded successfully")
                            }.addOnFailureListener {
                                Log.d("ChatViewModal", "Error in uploading chat List data ")
                            }
                    } else {
                        Log.d(
                            "ChatViewModal",
                            "Error in fetching chat image: ${task.exception?.message}"
                        )
                    }

                }
            }.addOnFailureListener {
                Log.d("ChatViewModal", "Error in uploading chat image")
            }
        }
    }

    private fun uploadTextData(message: String, isUser: Boolean, timestamp: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val onlineChatList = hashMapOf<String, Any>(
                "message" to message,
                "isUser" to isUser,
                "timestamp" to timestamp
            )
            firestore.collection("ChatBot")
                .document(uid).collection("ChatList").add(onlineChatList).addOnSuccessListener {
                    Log.d("ChatViewModal", "Chat List data uploaded successfully")
                }.addOnFailureListener {
                    Log.d("ChatViewModal", "Error in uploading chat List data ")
                }
        }
    }

    suspend fun checkChatData(context: Context, db: ChatDatabase) {
        val networkConnectivityChecker = NetworkConnectivityChecker(context)
        viewModelScope.launch(Dispatchers.IO) {
            fetchOfflineChatListData(db)
            networkConnectivityChecker.isNetworkAvailable().collect { isAvailable ->
                Log.d("ViewModel123", "Network available: $isAvailable")
                if (isAvailable && !isFetchingOnlineData) {
                    isFetchingOnlineData = true
                    fetchOnlineChatListData(db)
                }
            }
        }
    }

    private fun fetchOfflineChatListData(db: ChatDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            db.chatDao().getAllChats().collect { chatEntities ->
                val chatWithUris = chatEntities.reversed().map { entity ->
                    ChatWithUri(
                        entity.timestamp,
                        entity.message,
                        entity.imageAddress,
                        entity.isUser
                    )
                }
                _chatstate.update {
                    _iscircularloading.value = false
                    it.copy(
                        chatList = chatWithUris.toMutableList(),
                    )
                }

            }
        }
    }

    private fun fetchOnlineChatListData(db: ChatDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _iscircularloading.value = true
            }
            firestore.collection("ChatBot")
                .document(uid)
                .collection("ChatList")
                .orderBy("timestamp").get()
                .addOnSuccessListener { snapshot ->
                    viewModelScope.launch {
                        db.chatDao().deleteAllChats()
                    }

                    for (document in snapshot) {
                        val message = document.getString("message") ?: ""
                        val imageAddress = document.getString("imageAddress") ?: ""
                        val isUser = document.getBoolean("isUser") ?: false
                        val timestamp = document.getLong("timestamp") ?: 0

                        viewModelScope.launch {
                            db.chatDao()
                                .upsertChat(ChatEntity(timestamp, message, imageAddress, isUser))
                        }
                    }
                }.addOnFailureListener {
                    _iscircularloading.value = false
                    Log.d("ChatViewModal", "Error in fetching chat List data ")
                }
        }
    }

    fun deleteChatItem(timestamp: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("ChatBot").document(uid).collection("ChatList")
                .get().addOnSuccessListener { result ->
                    var deleted = false
                    for (document in result) {
                        if (document.data.get("timestamp").toString() == timestamp) {
                            firestore.collection("ChatBot").document(uid)
                                .collection("ChatList").document(document.id)
                                .delete().addOnSuccessListener {
                                    deleted = true
                                    onSuccess()
                                }.addOnFailureListener {
                                    Log.d(
                                        "delChat123",
                                        document.data.get("timestamp").toString() + timestamp
                                    )
                                }
                        }
                    }
                }.addOnFailureListener {
                    onFailure()
                }
        }
    }

    private fun Bitmap.toByteArray():
            ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 50, stream)
        return stream.toByteArray()
    }

    fun TimestampToTime(timestamp: Long): String {
        val date = Date(timestamp);
        val format = SimpleDateFormat("EEEE, hh:mm a")
        val time = format.format(date)
        return time
    }

}