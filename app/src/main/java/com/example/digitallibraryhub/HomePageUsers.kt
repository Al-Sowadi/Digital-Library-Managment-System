package com.example.digitallibraryhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_page_admin.*
import kotlinx.android.synthetic.main.activity_home_page_users.*

class HomePageUsers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {3
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_users)

        vbookUadminlayout.setOnClickListener{
            startActivity(Intent(this,ListBookActivity::class.java))
        }

        vSearchUbookdminlayout.setOnClickListener{
            startActivity(Intent(this,SearchBookByName::class.java))
        }
        vReadbookUdminlayout.setOnClickListener{
            startActivity(Intent(this,readBookAct::class.java))
        }
        userlgout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
//                FirebaseAuth.getInstance().signOut()
            val intent= Intent(this, LogInPage::class.java)
            Toast.makeText(this,"Logging Out", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()

        }
    }
}