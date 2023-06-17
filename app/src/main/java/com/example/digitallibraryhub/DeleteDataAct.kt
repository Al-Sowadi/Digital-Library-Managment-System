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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.card_delete_item.view.*
import kotlinx.android.synthetic.main.layout_list.view.*
import kotlinx.android.synthetic.main.layout_list.view.tvlink
import kotlinx.android.synthetic.main.layout_list.view.userName
import kotlinx.android.synthetic.main.layout_list.view.userStatus

class DeleteDataAct : AppCompatActivity() {
    private lateinit var mSearchTextd: EditText
    private lateinit var mRecyclerViewd: RecyclerView
    private lateinit var mDatabased: DatabaseReference
    private lateinit var mAdapterd: FirebaseRecyclerAdapter<booksad, BookViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_data)
        mSearchTextd = findViewById(R.id.searchTextd)
        mRecyclerViewd = findViewById(R.id.list_viewd)

        // Get database reference
        mDatabased = FirebaseDatabase.getInstance().getReference("AllDate/BooksInfo")

        // Set RecyclerView properties
        mRecyclerViewd.setHasFixedSize(true)
        mRecyclerViewd.layoutManager = LinearLayoutManager(this)

        // Define TextWatcher to trigger search
        mSearchTextd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = mSearchTextd.text.toString().trim()
                searchDatabase(searchText)
            }
        })

        // Query the database for all books
        val firebaseSearchQuery = mDatabased.orderByChild("aaname")
        val firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<booksad>()
            .setQuery(firebaseSearchQuery, booksad::class.java)
            .build()

        mAdapterd = object : FirebaseRecyclerAdapter<booksad, BookViewHolder>(firebaseRecyclerOptions) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.card_delete_item, parent, false)
                return BookViewHolder(view)
            }

            override fun onBindViewHolder(holder: BookViewHolder, position: Int, model: booksad) {
                // Bind book to ViewHolder
                holder.bindBook(model)
            }
        }

        mRecyclerViewd.adapter = mAdapterd
        mAdapterd.startListening()
    }


    // Search database for books that match search text
    private fun searchDatabase(searchText: String) {

        if (searchText.isEmpty()) {
            // Clear the RecyclerView
//            mAdapter.cleanup()
            mRecyclerViewd.adapter = mAdapterd
        } else {
            // Query the database for books that match the search text
            val firebaseSearchQuery = mDatabased.orderByChild("aaname")
                .startAt(capitalizeFirstWord(searchText))
                .endAt(capitalizeFirstWord(searchText) + "\uf8ff")


            // Use FirebaseRecyclerAdapter to display search results in RecyclerView
            val firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<booksad>()
                .setQuery(firebaseSearchQuery, booksad::class.java)
                .build()

            val adapter = object : FirebaseRecyclerAdapter<booksad, BookViewHolder>(firebaseRecyclerOptions) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.card_delete_item, parent, false)
                    return BookViewHolder(view)
                }

                override fun onBindViewHolder(holder: BookViewHolder, position: Int, model: booksad) {
                    // Bind search result to ViewHolder
                    holder.bindBook(model)
                }
            }

            mRecyclerViewd.adapter = adapter
            adapter.startListening()
        }
    }


    // ViewHolder class to display search results in RecyclerView
    class BookViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        // Bind book to ViewHolder
        fun bindBook(book: booksad?) {
            book?.let {

                val tvlink: TextView = itemView.findViewById(R.id.tvlink)
                val tlinkbut :TextView = itemView.findViewById(R.id.tlinkbut)
                view.userName.text = book.aaname
                view.userStatus.text = book.bbname
                view.tvlink.text = book.aaname
                view.tlinkbut.text = book.ttbook


                val vButton: ImageView = itemView.findViewById(R.id.a121)
                val deleteButton: ImageView = itemView.findViewById(R.id.a001) // new button for deleting data
                // Set OnClickListener for innerLayout view
                vButton
                    .setOnClickListener {
                    // Handle click event here
                    val intent = Intent(itemView.context, GetPdf::class.java)
                    intent.putExtra("id",tlinkbut.text)
                    itemView.context.startActivity(intent)
                }
                deleteButton.setOnClickListener {
                    val databaseReference = FirebaseDatabase.getInstance().getReference("AllDate/BooksInfo")
                    val query = databaseReference.orderByChild("aaname").equalTo(tvlink.text.toString())

                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val bookId = snapshot.key
                                val databaseReference = FirebaseDatabase.getInstance().getReference("AllDate/BooksInfo").child(bookId.toString())
                                databaseReference.removeValue()

                                // Do something with the bookId here
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error here
                        }
                    })

                }
            }
        }
    }
    fun capitalizeFirstWord(input: String): String {
        return input.toLowerCase().split(" ")
            .joinToString(" ") { it.capitalize() }
    }
}