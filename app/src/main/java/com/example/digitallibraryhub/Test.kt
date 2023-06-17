package com.example.digitallibraryhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.content.Intent
import android.net.Uri

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*

class Test : AppCompatActivity() {
    val PDF : Int = 0
    lateinit var uri : Uri
    lateinit var mStorage : StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        mStorage = FirebaseStorage.getInstance().getReference("Uploads")

        pdfBtn.setOnClickListener(View.OnClickListener {
                view: View? -> val intent = Intent()
            intent.type="application/pdf"
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
        })



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uriTxt = findViewById<View>(R.id.uriTxt) as TextView
        if (resultCode == RESULT_OK) {
            if (requestCode == PDF) {
                uri = data!!.data!!
                uriTxt.text = uri.toString()
                upload ()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun upload() {
        var mReference = mStorage.child(uri.lastPathSegment.toString())
        try {
            mReference.putFile(uri).addOnSuccessListener {
                    taskSnapshot: UploadTask.TaskSnapshot? ->
                val dwnTxt = findViewById<View>(R.id.dwnTxt) as TextView
                dwnTxt.text = ""
                Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
}