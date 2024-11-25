package com.example.snakesense

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImageDisplayActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        // Inicializa os componentes da activity
        imageView = findViewById(R.id.capturedImageView)
        val classifyButton: Button = findViewById(R.id.classify_button)
        val newPhotoButton: Button = findViewById(R.id.btnNewPhoto)

        // Recebe a imagem da MainActivity
        bitmap = intent.getParcelableExtra("imageBitmap") // Chave correta para a imagem
        bitmap?.let { imageView.setImageBitmap(it) }

        // Configurar o botão "Classificar"
        classifyButton.setOnClickListener {
            // Envia a imagem para a próxima Activity para a classificação
            val intent = Intent(this, ClassificationResultActivity::class.java)
            intent.putExtra("imageBitmap", bitmap) // Passa a imagem para a próxima tela
            startActivity(intent)
        }

        // Configurar o botão para capturar uma nova foto
        newPhotoButton.setOnClickListener {
            openCamera()
        }
    }

    // Método para abrir a câmera e capturar uma nova imagem
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Recebe a imagem capturada e a exibe na mesma activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Obtém o novo Bitmap da imagem capturada
            val newBitmap = data?.extras?.get("data") as Bitmap
            bitmap = newBitmap
            // Atualiza o ImageView com a nova imagem capturada
            imageView.setImageBitmap(newBitmap)
        }
    }
}
