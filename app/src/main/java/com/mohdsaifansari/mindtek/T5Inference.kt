package com.mohdsaifansari.mindtek

import android.content.res.AssetManager
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class T5Inference(assetManager: AssetManager, modelFilename: String) {
    private var interpreter: Interpreter
    private var gpuDelegate: GpuDelegate? = null
    var delegateUsed: String = "CPU" // default fallback

    init {
        // Load the TFLite model from assets.
        val modelBuffer = loadModelFile(assetManager, modelFilename)
        val options = Interpreter.Options()

        val gpuDelegateInstance: GpuDelegate? = try {
            // [CHANGE] Using no-argument constructor for GPU delegate.
            GpuDelegate()
        } catch (e: Exception) {
            Log.e("DeviceCheck", "GPU delegate is not supported: ${e.message}")
            null
        }

        if (gpuDelegateInstance != null) {
            options.apply {
                addDelegate(gpuDelegateInstance)
                // Use as many threads as available processors.
                numThreads = Runtime.getRuntime().availableProcessors()
            }
            delegateUsed = "GPU"
            gpuDelegate = gpuDelegateInstance // [CHANGE] Save delegate for later closing.
        } else {
            options.apply {
                // Use as many threads as available processors.
                numThreads = Runtime.getRuntime().availableProcessors()
            }
            delegateUsed = "NNAPI/CPU (XNNPACK)"
        }
        interpreter = Interpreter(modelBuffer, options)
        Log.d("T5Inference", "Interpreter initialized using delegate: $delegateUsed")
    }


    private fun loadModelFile(assetManager: AssetManager, modelFilename: String): MappedByteBuffer {
        // Open the file descriptor for the TFLite model.
        val fileDescriptor = assetManager.openFd(modelFilename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        // Memory-map the file for efficient access.
        return inputStream.channel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    /**
     * Runs inference on the T5 model.
     *
     * @param encoderInputIds A 2D int array of shape [1, encoderMaxLength]
     * @param encoderAttentionMask A 2D int array of shape [1, encoderMaxLength]
     * @param decoderInputIds A 2D int array of shape [1, decoderMaxLength]
     * @return A 3D float array of shape [1, decoderMaxLength, vocabSize] containing the output logits.
     */
    fun runInference(
        encoderInputIds: Array<IntArray>,
        encoderAttentionMask: Array<IntArray>,
        decoderInputIds: Array<IntArray>
    ): Array<Array<FloatArray>> {
        // Set vocabSize to match your training vocabulary.
        val vocabSize = 32128
        val decoderMaxLength = decoderInputIds[0].size

        // Allocate the output array for the logits.
        val outputLogits = Array(1) { Array(decoderMaxLength) { FloatArray(vocabSize) } }

        Log.d("T5Inference", "Running inference: encoder shape: ${encoderInputIds.size} x ${encoderInputIds[0].size}, " +
                "decoder shape: ${decoderInputIds.size} x ${decoderInputIds[0].size}")

        // Run inference using the TFLite Interpreter.
        interpreter.runForMultipleInputsOutputs(
            arrayOf(encoderInputIds, encoderAttentionMask, decoderInputIds),
            mapOf(0 to outputLogits)
        )

        Log.d("T5Inference", "Inference completed.")
        return outputLogits
    }
    fun close() {
        interpreter.close()
        gpuDelegate?.close()
        Log.d("T5Inference", "Interpreter and GPU delegate closed.")
    }
}
