package com.example.digitallibraryhub

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_log_in_page.*
import java.util.jar.Attributes.Name

class LogInPage : AppCompatActivity() {
    //create a button variable
    lateinit var log_google: ImageView
    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN=234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_page)
        auth = Firebase.auth
        ///code for singUp
        supportActionBar?.hide()
        var boolean = true
        singUp.setOnClickListener {
            singUp.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUp.setTextColor(resources.getColor(R.color.btncolorcode,null))
            logIn.background = null
            singUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            logIn.setTextColor(resources.getColor(R.color.tbutcolr,null))
            singIn.text = "Sing Up"
            boolean = false
        }
        // Forget_Password
        Forget_Password.setOnClickListener{
            Intent(this, ResetPassword::class.java).also { startActivity(it) }
        }
        ///code for logIn
        logIn.setOnClickListener {
            singUp.background = null
            singUp.setTextColor(resources.getColor(R.color.tbutcolr,null))
            logIn.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            logIn.setTextColor(resources.getColor(R.color.btncolorcode,null))
            singIn.text = "log In"
            boolean = true
        }
        /// btn login
        singIn.setOnClickListener{
            if (boolean == true) {
                val email = eMail.text.toString()
                val pass = passwords.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {

                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            if (email == "alsowadi27@gmail.com"){
                                val intent = Intent(this, HomePageAdmin::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                val intent = Intent(this, HomePageUsers::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

                }
            }else{
                val passwordss = findViewById<TextInputEditText>(R.id.passwordss)
                when {
                    TextUtils.isEmpty(eName.text.toString().trim { it <= ' ' }) -> {
                        eName.setError("Please Enter First Name")
                        eName.requestFocus()
                    }
                    TextUtils.isEmpty(eMails.text.toString().trim { it <= ' ' }) -> {
                        eMails.setError("Please Enter Last Name")
                        eMails.requestFocus()
                    }
                    TextUtils.isEmpty(passwordss.text.toString().trim { it <= ' ' }) -> {
                        passwordss.setError("Please Enter password")
                        passwordss.requestFocus()
                    }
                    TextUtils.isEmpty(passwords01.text.toString().trim { it <= ' ' }) -> {
                        passwords01.setError("Please Enter password")
                        passwords01.requestFocus()
                    }
                    else -> {
                        if (passwordss.text.toString() != passwords01.text.toString()) {
                            Toast.makeText(applicationContext, "Passcode doesn't match", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            // Get the Firebase Authentication instance
                            val mAuth = FirebaseAuth.getInstance()

                            // Check if the email already exists
                            mAuth.fetchSignInMethodsForEmail(eMails.text.toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val result = task.result
                                        val signInMethods = result?.signInMethods
                                        if (signInMethods != null && signInMethods.contains(
                                                EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD
                                            )
                                        ) {
                                            // The user exists and can be signed in with email and password
                                            Toast.makeText(
                                                applicationContext,
                                                "The user exists and can be signed in with email and password",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {

                                            auth.createUserWithEmailAndPassword(
                                                eMails.text.toString(),
                                                passwordss.text.toString()
                                            )
                                                .addOnCompleteListener(this) { task ->
                                                    if (task.isSuccessful) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                                                        val user = auth.currentUser
                                                        AddUserData( eMails.text.toString(),
                                                            eName.text.toString())
                                                        updateUI(user)
                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Log.w(
                                                            ContentValues.TAG,
                                                            "createUserWithEmail:failure",
                                                            task.exception
                                                        )
                                                        Toast.makeText(
                                                            baseContext, "Authentication failed.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        updateUI(null)
                                                    }
                                                }
                                        }
                                    } else {
                                        // An error occurred
                                        Toast.makeText(
                                            applicationContext,
                                            "An error occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                }
            }
        }
        ///btn login end
        //assign the xml view to the button
        log_google= findViewById(R.id.log_google)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //now set click listener for button
        log_google.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }

    }
    //on activity result altenative
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                if (task.isSuccessful) {
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
//                 Google Sign In failed, update UI appropriately
                        Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()

                    }
                }


            }
        }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    //send user to main
                    gotomain()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "failed to sign in", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun gotomain()
    {
        val intent = Intent(this, HomePageUsers::class.java)
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            AddUserData(name.toString(),email.toString())
            val uid = it.uid
        }
        startActivity(intent)
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser!=null)
        {
            //user exists so direct him to another activity
            gotomain()
        }
    }
    fun AddUserData(eName:String,eMails:String){
        val Userobj = users(
            eName,
            eMails
        )
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference("AllDate/users")
                .child(
                    it
                ).setValue(Userobj).addOnCompleteListener { task ->
                    if (task.isSuccessful){

                        Toast.makeText(
                            this,
                            "Use added successfully",
                            Toast.LENGTH_LONG
                        ).show()

                    }else{
                        Toast.makeText(
                            this,
                            "Use did not add successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomePageUsers::class.java))
            finish()
        } else {
            Toast.makeText(this, "You Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }
}