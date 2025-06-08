package com.mohdsaifansari.mindtek

import android.util.Log

object SentencePieceTokenizer {
    init {
        try {
            System.loadLibrary("sentencepiece_jni")
            Log.d("SentencePieceTokenizer", "Native library loaded successfully")
        } catch (e: Exception) {
            Log.e("SentencePieceTokenizer", "Failed to load native library: ${e.message}")
            throw e
        }
    }
    external fun nativeLoadModel(modelPath: String): Boolean
    external fun nativeEncode(inputText: String): IntArray
    external fun nativeDecode(ids: IntArray): String
    external fun nativePadId(): Int
    external fun nativeEosId(): Int

    fun loadModel(modelPath: String): Boolean {
        Log.d("SentencePieceTokenizer", "Loading model from: $modelPath")
        return nativeLoadModel(modelPath)
    }
    fun encode(input: String): List<Int> = nativeEncode(input).toList()
    fun decode(ids: List<Int>): String = nativeDecode(ids.toIntArray())
    val padId: Int get() = nativePadId()
    val eosId: Int get() = nativeEosId()
}
