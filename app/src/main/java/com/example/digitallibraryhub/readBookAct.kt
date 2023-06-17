package com.example.digitallibraryhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.layout_list.view.*

class readBookAct : AppCompatActivity() {
    private lateinit var mSearchTextr: EditText
    private lateinit var mRecyclerViewr: RecyclerView
    private lateinit var mDatabaser: DatabaseReference
    private lateinit var mAdapterr: FirebaseRecyclerAdapter<booksad, BookViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_book)
        mSearchTextr = findViewById(R.id.searchTextr)
        mRecyclerViewr = findViewById(R.id.list_viewr)

        // Get database reference
        mDatabaser = FirebaseDatabase.getInstance().getReference("AllDate/BooksInfo")

        // Set RecyclerView properties
        mRecyclerViewr.setHasFixedSize(true)
        mRecyclerViewr.layoutManager = LinearLayoutManager(this)

        // Define TextWatcher to trigger search
        mSearchTextr.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = mSearchTextr.text.toString().trim()
                searchDatabase(searchText)
            }
        })

        // Query the database for all books
        val firebaseSearchQuery = mDatabaser.orderByChild("aaname")
        val firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<booksad>()
            .setQuery(firebaseSearchQuery, booksad::class.java)
            .build()

        mAdapterr = object : FirebaseRecyclerAdapter<booksad, BookViewHolder>(firebaseRecyclerOptions) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list, parent, false)
                return BookViewHolder(view)
            }

            override fun onBindViewHolder(holder: BookViewHolder, position: Int, model: booksad) {
                // Bind book to ViewHolder
                holder.bindBook(model)
            }
        }

        mRecyclerViewr.adapter = mAdapterr
        mAdapterr.startListening()

    }
    private fun searchDatabase(searchText: String) {

        if (searchText.isEmpty()) {
            // Clear the RecyclerView
//            mAdapter.cleanup()
            mRecyclerViewr.adapter = mAdapterr
        } else {
            // Query the database for books that match the search text
            val firebaseSearchQuery = mDatabaser.orderByChild("aaname")
                .startAt(capitalizeFirstWord(searchText))
                .endAt(capitalizeFirstWord(searchText) + "\uf8ff")


            // Use FirebaseRecyclerAdapter to display search results in RecyclerView
            val firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<booksad>()
                .setQuery(firebaseSearchQuery, booksad::class.java)
                .build()

            val adapter = object : FirebaseRecyclerAdapter<booksad, BookViewHolder>(firebaseRecyclerOptions) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list, parent, false)
                    return BookViewHolder(view)
                }

                override fun onBindViewHolder(holder: BookViewHolder, position: Int, model: booksad) {
                    // Bind search result to ViewHolder
                    holder.bindBook(model)
                }
            }

            mRecyclerViewr.adapter = adapter
            adapter.startListening()
        }
    }

    // ViewHolder class to display search results in RecyclerView
    class BookViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        // Bind book to ViewHolder
        fun bindBook(book: booksad?) {
            book?.let {

                val tvlink: TextView = itemView.findViewById(R.id.tvlink)

                view.userName.text = book.aaname
                view.userStatus.text = book.bbname
                view.tvlink.text = book.ttbook
                val a001: ImageView = itemView.findViewById(R.id.a001)


                // Set OnClickListener for innerLayout view
                a001.setOnClickListener {
                    // Handle click event here
                    val intent = Intent(itemView.context, GetPdf::class.java)
                    intent.putExtra("id",tvlink.text)
                    itemView.context.startActivity(intent)
                }



            }
        }
    }

    fun capitalizeFirstWord(input: String): String {
        return input.toLowerCase().split(" ")
            .joinToString(" ") { it.capitalize() }
    }
}