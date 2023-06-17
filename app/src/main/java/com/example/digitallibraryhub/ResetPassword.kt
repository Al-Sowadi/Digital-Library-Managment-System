package com.example.digitallibraryhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val fAuth = FirebaseAuth.getInstance()
        reset_password.setOnClickListener{
            fAuth.sendPasswordResetEmail(eMailReset.text.toString()).addOnCompleteListener { listener ->
                if (listener.isSuccessful) {
                    Intent(this, LogInPage::class.java).also { startActivity(it) }
                    Toast.makeText(
                        this,
                        "Email sent successful check inbox to RESET password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Email doesn't send successful ,Email doesn't exist chick user name agine",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Do something when not successful
                }
            }
        }
        cancelbtn.setOnClickListener{
            Intent(this, LogInPage::class.java).also { startActivity(it) }
        }

    }
}