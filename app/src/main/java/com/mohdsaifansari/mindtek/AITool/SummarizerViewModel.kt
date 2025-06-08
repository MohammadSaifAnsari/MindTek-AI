package com.mohdsaifansari.mindtek.AITool

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.ViewModel
import com.mohdsaifansari.mindtek.AITool.SummarizerViewModel.Beam
import com.mohdsaifansari.mindtek.SentencePieceTokenizer
import com.mohdsaifansari.mindtek.T5Inference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ln



class SummarizerViewModel : ViewModel() {
    companion object {
        private const val TAG = "SummarizerModel"
        private const val DECODER_MAX_LENGTH = 150
        // Adjust special token IDs as per your SentencePiece model.
        const val DECODER_START_TOKEN_ID = 0 // typically <pad>
        const val EOS_TOKEN_ID = 1           // typically </s>
    }

    private var _isModelResult = MutableStateFlow<String>(" ")
    val isModelResult: StateFlow<String> = _isModelResult.asStateFlow()

    private val _isloadingAnimation = MutableStateFlow<Boolean>(false)
    val isloadingAnimation: StateFlow<Boolean> = _isloadingAnimation.asStateFlow()

    // Data class representing a beam (stores token sequence and cumulative log probability).
    data class Beam(val sequence: List<Int>, val score: Float)



    suspend fun runSummarizationWithBatchedBeam(
        spModelPath: String,
        inputText: String,
        assets: AssetManager,
        decoderMaxLength: Int = 150,
        beamWidth: Int = 4,
        diversityPenalty: Float = 0.5f
    ): String {
        return withContext(Dispatchers.IO) {
            _isloadingAnimation.value = true
            // Load SentencePiece model.
            if (!SentencePieceTokenizer.loadModel(spModelPath)) {
                Log.e("Summarizer", "Failed to load SentencePiece model")
                return@withContext "Error: Failed to load SentencePiece model"
            } else {
                Log.d("Summarizer", "SentencePiece model loaded successfully")
            }

            // Create T5Inference instance.
            val t5Inference = T5Inference(assets, "t5_model_with_decoder.tflite")
            Log.d("Summarizer", "T5Inference instance created.")

            // Build the full input prompt.
            val fullInput = "summarize: $inputText"
            Log.d("Summarizer", "Full Input Text: $fullInput")

            // Tokenize input text.
            val tokenIds = SentencePieceTokenizer.encode(fullInput)
            Log.d("Summarizer", "Encoded Token IDs: $tokenIds")
            if (tokenIds.isEmpty()) {
                Log.e("Summarizer", "Tokenization returned empty list")
                return@withContext "Error: Tokenization failed"
            }

            // Prepare encoder inputs (fixed to 1500 tokens).
            val encoderMaxLength = 1500
            val encoderIds = IntArray(encoderMaxLength) { SentencePieceTokenizer.padId }
            val attentionMask = IntArray(encoderMaxLength) { 0 }
            var i = 0
            for (id in tokenIds) {
                if (i >= encoderMaxLength) break
                encoderIds[i] = id
                attentionMask[i] = 1
                i++
            }
            if (i < encoderMaxLength) {
                encoderIds[i] = SentencePieceTokenizer.eosId
                attentionMask[i] = 1
            }
            Log.d("Summarizer", "Prepared encoderIds and attentionMask")

            // Initialize beam search with one beam starting with the decoder start token.
            var beams = listOf(
                SummarizerViewModel.Beam(
                    listOf(SummarizerViewModel.DECODER_START_TOKEN_ID),
                    0f
                )
            )

            // Iterate over decoding steps.
            for (step in 1 until decoderMaxLength) {
                val allCandidates = mutableListOf<SummarizerViewModel.Beam>()
                // Filter active beams (those not ending with EOS).
                val activeBeams = beams.filter { it.sequence.last() != SummarizerViewModel.EOS_TOKEN_ID }
                if (activeBeams.isEmpty()) break

                // Build a batch of decoder inputs for all active beams.
                val batchSize = activeBeams.size
                val batchDecoderInputs = Array(batchSize) { IntArray(decoderMaxLength) { SentencePieceTokenizer.padId } }
                activeBeams.forEachIndexed { index, beam ->
                    beam.sequence.forEachIndexed { seqIndex, token ->
                        batchDecoderInputs[index][seqIndex] = token
                    }
                }
                // Create batch for encoder: replicate encoderIds and attentionMask.
                val batchEncoderInputs = Array(batchSize) { encoderIds }
                val batchAttentionMasks = Array(batchSize) { attentionMask }

                // Run inference once for all active beams.
                val logitsBatch = t5Inference.runInference(
                    batchEncoderInputs, batchAttentionMasks, batchDecoderInputs
                )
                // logitsBatch shape: [batchSize, decoderMaxLength, vocabSize]

                // For diversity penalty, count how many beams choose each candidate token.
                val tokenCounts = mutableMapOf<Int, Int>()
                // For each beam, determine the candidate token (via argmax) for the current step.
                val candidatesPerBeam = activeBeams.mapIndexed { index, _ ->
                    val currentLogits = logitsBatch[index][step - 1]
                    val nextToken = currentLogits.indices.maxByOrNull { currentLogits[it] } ?: 0
                    tokenCounts[nextToken] = tokenCounts.getOrDefault(nextToken, 0) + 1
                    Pair(activeBeams[index], nextToken)
                }

                // Update beams with new candidates.
                for ((beam, token) in candidatesPerBeam) {
                    // Get probabilities via softmax for the current step.
                    val currentLogits = logitsBatch[activeBeams.indexOf(beam)][step - 1]
                    val probabilities = softmax(currentLogits)
                    val tokenProb = probabilities[token]
                    // Compute new beam score.
                    var newScore = beam.score + ln(tokenProb.toDouble()).toFloat()
                    val count = tokenCounts[token] ?: 1
                    if (count > 1) {
                        newScore -= diversityPenalty * (count - 1)
                    }
                    val newSequence = beam.sequence + token
                    allCandidates.add(SummarizerViewModel.Beam(newSequence, newScore))
                    Log.d("Summarizer", "Step $step: Beam updated with token id: $token (score: $newScore)")
                }
                // Include beams that already ended with EOS.
                val finishedBeams = beams.filter { it.sequence.last() == SummarizerViewModel.EOS_TOKEN_ID }
                beams = (allCandidates + finishedBeams)
                    .sortedByDescending { normalizedScore(it) }
                    .take(beamWidth)
                Log.d("Summarizer", "Step $step: Current beams: ${beams.map { it.sequence }}")
                if (beams.all { it.sequence.last() == SummarizerViewModel.EOS_TOKEN_ID }) break
            }
            // Select the best beam.
            val bestBeam = beams.maxByOrNull { normalizedScore(it) } ?: beams.first()
            val decodedText = SentencePieceTokenizer.decode(bestBeam.sequence)
            Log.d("Summarizer", "Final Decoded Text: $decodedText")
            t5Inference.close()
            _isModelResult.value = decodedText
            _isloadingAnimation.value = false
            decodedText
        }
    }
}



// Top-level function to compute a normalized score for beam search.
// Normalization (dividing cumulative log probability by sequence length) helps compare beams of different lengths.
private fun normalizedScore(beam: Beam): Float {
    return beam.score / beam.sequence.size.toFloat()
}




// Helper function: Compute softmax over a FloatArray.
private fun softmax(logits: FloatArray): FloatArray {
    val maxLogit = logits.maxOrNull() ?: 0f
    val exps = logits.map { java.lang.Math.exp((it - maxLogit).toDouble()).toFloat() }
    val sumExps = exps.sum()
    return exps.map { it / sumExps }.toFloatArray()
}

