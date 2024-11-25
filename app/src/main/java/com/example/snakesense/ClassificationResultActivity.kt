package com.example.snakesense

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.widget.Button
import android.widget.TextView
import com.example.snakesense.ml.TFLiteModel

class ClassificationResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classification_result)

        val resultText: TextView = findViewById(R.id.result_text)
        val backButton: Button = findViewById(R.id.back_button)

        // Receber a imagem da ImageDisplayActivity
        val bitmap: Bitmap? = intent.getParcelableExtra("image_bitmap")

        // Carregar o modelo
        val modelFile = assets.openFd("meu_modelo.tflite").createInputStream()
        val model = TFLiteModel(TFLiteModel.loadModelFile(modelFile))

        // Realizar a classificação
        bitmap?.let {
            val result = model.classifyImage(it)
            resultText.text = "Classificação: $result"
        }

        // Configurar botão "Voltar"
        backButton.setOnClickListener {
            finish()
        }
    }
}