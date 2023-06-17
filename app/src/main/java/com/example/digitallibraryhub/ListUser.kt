package com.example.digitallibraryhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        val database1 = FirebaseDatabase.getInstance()
        val myRef1 = database1.getReference("AllDate/users")


        val recyclerview1 = findViewById<RecyclerView>(R.id.recyclerview1)
        recyclerview1.layoutManager = LinearLayoutManager(this)
        val datauser = ArrayList<userclase>()
        var getdata1 = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    var m22 = i.child("email").getValue()
                    var m11 = i.child("name").getValue()
//                        Toast.makeText(this@ListUser,"${m11.toString()}",Toast.LENGTH_SHORT).show()
                    datauser.add(userclase(m11.toString(),m22.toString()))

                }
                val adapter1 = listuserAdapter(datauser)

                // Setting the Adapter with the recyclerview
                recyclerview1.adapter = adapter1
            }
        }
//        myRef1.addValueEventListener(getdata1)
        myRef1.addListenerForSingleValueEvent(getdata1)
    }
}