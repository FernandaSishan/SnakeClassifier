package com.example.snakesense.ml

import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TFLiteModel(modelBuffer: MappedByteBuffer) {

    private val interpreter: Interpreter = Interpreter(modelBuffer)

    // Dimensões esperadas do modelo
    private val inputSize = 224
    private val imageMean = 127.5f
    private val imageStd = 127.5f

    // Método para classificar a imagem
    fun classifyImage(bitmap: Bitmap): String {
        // Pré-processa a imagem
        val input = preprocessImage(bitmap)

        // Resultado: ajustado para um modelo de saída com 2 classes
        val output = Array(1) { FloatArray(2) }

        // Executa a inferência
        interpreter.run(input, output)

        // Interpreta o resultado
        val venomousScore = output[0][0]
        val nonVenomousScore = output[0][1]
        return if (venomousScore > nonVenomousScore) {
            "Venenosa (Confiança: ${(venomousScore * 100).toInt()}%)"
        } else {
            "Não venenosa (Confiança: ${(nonVenomousScore * 100).toInt()}%)"
        }
    }

    // Pré-processa a imagem para adequá-la ao modelo
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputSize * inputSize)
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)

        for (pixel in intValues) {
            val r = (pixel shr 16 and 0xFF) / imageStd - imageMean
            val g = (pixel shr 8 and 0xFF) / imageStd - imageMean
            val b = (pixel and 0xFF) / imageStd - imageMean
            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        return byteBuffer
    }

    companion object {
        // Carrega o modelo TFLite do arquivo
        fun loadModelFile(assetFileDescriptor: AssetFileDescriptor): MappedByteBuffer {
            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }
}