package com.example.snakesense

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ClassificationActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classification)

        val imageView = findViewById<ImageView>(R.id.capturedImageView)
        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        imageView.setImageBitmap(imageBitmap)

        val btnNewPhoto = findViewById<Button>(R.id.btnNewPhoto)
        btnNewPhoto.setOnClickListener {
            openCamera()
        }

        val inicio = findViewById<Button>(R.id.inicio)
        inicio.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Método para abrir a câmera novamente
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

}