package com.mhrlive.pikpok

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mhrlive.pikpok.data.Post
import java.io.File
import java.util.UUID

class UploadActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var captionEditText: EditText
    private lateinit var selectImageButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var uploadButton: Button
    
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2
    private val CAMERA_PERMISSION_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        imageView = findViewById(R.id.imageView)
        captionEditText = findViewById(R.id.captionEditText)
        selectImageButton = findViewById(R.id.selectImageButton)
        takePictureButton = findViewById(R.id.takePictureButton)
        uploadButton = findViewById(R.id.uploadButton)
    }

    private fun setupClickListeners() {
        selectImageButton.setOnClickListener {
            selectImageFromGallery()
        }

        takePictureButton.setOnClickListener {
            if (checkCameraPermission()) {
                takePicture()
            } else {
                requestCameraPermission()
            }
        }

        uploadButton.setOnClickListener {
            uploadPost()
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile = File(externalCacheDir, "photo_${System.currentTimeMillis()}.jpg")
            selectedImageUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
            startActivityForResult(intent, CAMERA_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    selectedImageUri = data?.data
                    displaySelectedImage()
                }
                CAMERA_REQUEST -> {
                    displaySelectedImage()
                }
            }
        }
    }

    private fun displaySelectedImage() {
        selectedImageUri?.let { uri ->
            Glide.with(this)
                .load(uri)
                .into(imageView)
        }
    }

    private fun uploadPost() {
        val caption = captionEditText.text.toString().trim()
        val currentUser = auth.currentUser

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentUser == null) {
            Toast.makeText(this, "Please log in to upload", Toast.LENGTH_SHORT).show()
            return
        }

        uploadButton.isEnabled = false
        uploadButton.text = "Uploading..."

        // Upload image to Firebase Storage
        val imageRef = storage.reference.child("posts/${UUID.randomUUID()}.jpg")
        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    savePostToFirestore(downloadUri.toString(), caption, currentUser.uid)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                uploadButton.isEnabled = true
                uploadButton.text = "Upload Post"
            }
    }

    private fun savePostToFirestore(imageUrl: String, caption: String, userId: String) {
        // First get user info
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { userDoc ->
                val username = userDoc.getString("username") ?: "Unknown"
                val userProfilePicture = userDoc.getString("profilePictureUrl") ?: ""

                val post = Post(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    username = username,
                    userProfilePicture = userProfilePicture,
                    imageUrl = imageUrl,
                    caption = caption,
                    timestamp = System.currentTimeMillis(),
                    likesCount = 0,
                    commentsCount = 0
                )

                firestore.collection("posts")
                    .document(post.id)
                    .set(post)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save post: ${e.message}", Toast.LENGTH_SHORT).show()
                        uploadButton.isEnabled = true
                        uploadButton.text = "Upload Post"
                    }
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                Toast.makeText(this, "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show()
            }
        }
    }
}