#include <jni.h>
#include "sentencepiece_processor.h"
#include <string>
#include <vector>

using sentencepiece::SentencePieceProcessor;

static SentencePieceProcessor* sp = nullptr;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_mohdsaifansari_mindtek_SentencePieceTokenizer_nativeLoadModel(JNIEnv* env, jobject thiz, jstring modelPath) {
    const char* path = env->GetStringUTFChars(modelPath, nullptr);
    sp = new SentencePieceProcessor();
    auto status = sp->Load(path);
    env->ReleaseStringUTFChars(modelPath, path);
    return status.ok() ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jintArray JNICALL
Java_com_mohdsaifansari_mindtek_SentencePieceTokenizer_nativeEncode(JNIEnv* env, jobject thiz, jstring inputText) {
    const char* text = env->GetStringUTFChars(inputText, nullptr);
    std::vector<int> ids;
    sp->Encode(text, &ids);
    env->ReleaseStringUTFChars(inputText, text);
    jintArray result = env->NewIntArray(ids.size());
    env->SetIntArrayRegion(result, 0, ids.size(), ids.data());
    return result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_mohdsaifansari_mindtek_SentencePieceTokenizer_nativeDecode(JNIEnv* env, jobject thiz, jintArray idsArray) {
    jsize length = env->GetArrayLength(idsArray);
    std::vector<int> ids(length);
    env->GetIntArrayRegion(idsArray, 0, length, ids.data());
    std::string decoded;
    sp->Decode(ids, &decoded);
    return env->NewStringUTF(decoded.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_mohdsaifansari_mindtek_SentencePieceTokenizer_nativePadId(JNIEnv* env, jobject thiz) {
    return sp->pad_id();
}

extern "C" JNIEXPORT jint JNICALL
Java_com_mohdsaifansari_mindtek_SentencePieceTokenizer_nativeEosId(JNIEnv* env, jobject thiz) {
    return sp->eos_id();
}

