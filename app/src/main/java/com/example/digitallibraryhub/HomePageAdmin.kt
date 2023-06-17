package com.example.digitallibraryhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_page_admin.*

class HomePageAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_admin)
        vbookadminlayout.setOnClickListener{
            startActivity(Intent(this,ListBookActivity::class.java))
        }
        vUseradminlayout.setOnClickListener{
            startActivity(Intent(this,ListUser::class.java))
        }
        vAddbookdminlayout.setOnClickListener{
            startActivity(Intent(this,AdminAddBook::class.java))
        }
        vSearchbookdminlayout.setOnClickListener{
            startActivity(Intent(this,SearchBookByName::class.java))
        }
        vReadbookdminlayout.setOnClickListener{
            startActivity(Intent(this,readBookAct::class.java))
        }
        vDeletebookdminlayout.setOnClickListener{
            startActivity(Intent(this,DeleteDataAct::class.java))
        }
        adminlgout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
//                FirebaseAuth.getInstance().signOut()
            val intent= Intent(this, LogInPage::class.java)
            Toast.makeText(this,"Logging Out", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()

        }
    }
}