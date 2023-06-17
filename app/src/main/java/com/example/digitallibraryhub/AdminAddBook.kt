package com.example.digitallibraryhub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_admin_add_book.*
import java.lang.Math.abs
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


class AdminAddBook : AppCompatActivity() {
    val PDF : Int = 0
    lateinit var uri : Uri
    lateinit var mStorage : StorageReference
    lateinit var uriTxt :String
    lateinit var dropdown:Spinner
    private var selectedItemOp: String = "General Knowledge"
    lateinit var firebaseDatabase: FirebaseDatabase

    // creating a variable for our Database
    // Reference for Firebase.
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_book)
        mStorage = FirebaseStorage.getInstance().getReference("Uploads")
        firebaseDatabase = FirebaseDatabase.getInstance();
        ///
        backbtnAddBook.setOnClickListener{
            startActivity(Intent(this,HomePageAdmin::class.java))
        }
// Create an ArrayAdapter using the string array and a default spinner layout
        dropdown = findViewById(R.id.spinner)

// Create an ArrayAdapter using the string array and a default spinner layout

        val items = arrayOf("General Knowledge", "Academics", "Fiction", "History")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter
        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                val selectedItem =
                selectedItemOp = items[position]
                Toast.makeText(
                    applicationContext,
                    "Selected item: $selectedItemOp",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("AllDate/BooksInfo");
//        categoryBtnAddBookAdmin.setOnClickListener{
//            BooksInfoFun("1","2","downloadUrl","uriTxt")
//            Toast.makeText(this,"yes",Toast.LENGTH_SHORT).show()
//        }
        categoryBtnAddBookAdmin.setOnClickListener(View.OnClickListener {
                view: View? -> val intent = Intent()
            intent.type = "application/pdf"
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
        })
    }
    fun BooksInfoFun(
        BName: String,
        AName: String,
        Burl: String,
        DBook: String,
        Tbook: String,
        Burl2: String,
        bookId: Int,
        UTime:  String
    ){
        val Userobj = BooksInfo(BName,AName,DBook,Tbook,Burl,Burl2,bookId,UTime)
        val newUUID = UUID.randomUUID().toString()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.

                databaseReference.child(newUUID).setValue(Userobj)

                // after adding this data we are showing toast message.
                Toast.makeText(this@AdminAddBook, "data added", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(@NonNull error: DatabaseError) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(this@AdminAddBook, "Fail to add data $error", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }
    fun randomUUID() = UUID.randomUUID().toString()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {
            if (requestCode == PDF) {
                uri = data!!.data!!
                uriTxt = uri.toString()
                upload ()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun upload() {
        var mReference = mStorage.child(uri.lastPathSegment.toString())
        try {
            var bookId = abs(Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).nextLong())
            var UTime = LocalDate.now().toString()
            mReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    BooksInfoFun(categoryEtAddBookAdmin.text.toString(),categoryEtAutorAAddBookAdmin.text.toString(),categoryEtDiscAAddBookAdmin.text.toString(),
                        selectedItemOp,downloadUrl,uriTxt,bookId.toInt(),UTime)

                    Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
                }
            }
        }catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}