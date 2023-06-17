package com.example.digitallibraryhub

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_book.*


class AddBook : AppCompatActivity() {

    private lateinit var storageRef: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        // Get a reference to the Firebase Storage bucket
        storageRef = FirebaseStorage.getInstance().getReference()



        UploadPdfbtn.setOnClickListener {
            // Create an intent to select a file
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"

            // Start the activity to select a file
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected file
            val fileUri = data.data as Uri
            val fileName = fileUri.lastPathSegment

            // Create a reference to the file in Firebase Storage
            val fileRef = storageRef.child("files/$fileName")

            // Upload the file to Firebase Storage
            fileRef.putFile(fileUri).addOnSuccessListener {
                // Get the download URL of the uploaded file
                fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Save additional details about the uploaded file in Firebase Firestore
                    val db = FirebaseFirestore.getInstance()
                    val document = db.collection("files").document(fileName.toString())
                    val details = hashMapOf("fileName" to fileName, "downloadUrl" to downloadUrl.toString(),"Type" to "pdf")
                    document.set(details).addOnSuccessListener {
                        // File details saved successfully
                    }.addOnFailureListener {
                        // File details failed to save
                    }
                }
            }.addOnFailureListener {
                // File upload failed
            }
        }
    }
}