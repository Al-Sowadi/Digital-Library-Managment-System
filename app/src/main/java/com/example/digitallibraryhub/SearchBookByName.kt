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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_list.view.*

class SearchBookByName : AppCompatActivity() {

    private lateinit var mSearchText: EditText
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mDatabase: DatabaseReference
    private var mAdapter: FirebaseRecyclerAdapter<booksad, BookViewHolder>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_book_by_name)
        // Get references to views
        mSearchText = findViewById(R.id.searchText)
        mRecyclerView = findViewById(R.id.list_view)

        // Get database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("AllDate/BooksInfo")

        // Set RecyclerView properties
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Define TextWatcher to trigger search
        mSearchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val searchText = mSearchText.text.toString().trim()
                searchDatabase(searchText)
            }
        })
    }

    // Search database for books that match search text
    private fun searchDatabase(searchText: String) {

        if (searchText.isEmpty()) {
            // Clear the RecyclerView
//            mAdapter.cleanup()
            mRecyclerView.adapter = mAdapter
        } else {
            // Query the database for books that match the search text
            val firebaseSearchQuery = mDatabase.orderByChild("aaname")
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

            mRecyclerView.adapter = adapter
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
                val innerLayout: LinearLayout = itemView.findViewById(R.id.cv_scheme)

                // Set OnClickListener for innerLayout view
                innerLayout.setOnClickListener {
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
