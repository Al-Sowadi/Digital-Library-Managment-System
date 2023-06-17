package com.example.digitallibraryhub


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class MyAdapter(private val userList : ArrayList<booksad> ,private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]

        holder.firstName.text = currentitem.aaname
        holder.lastName.text = currentitem.bbname
        holder.age.text = currentitem.utime
        holder.ttbook.text = currentitem.ttbook
        holder.firstName.setOnClickListener {
//            val intent = Intent(context, GetPdf::class.java)
//            intent.putExtra("id",holder.firstName.text.toString())
//            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val firstName : TextView = itemView.findViewById(R.id.tvfirstName)
        val lastName : TextView = itemView.findViewById(R.id.tvlastName)
        val age : TextView = itemView.findViewById(R.id.tvage)
        val ttbook :TextView = itemView.findViewById(R.id.tvlink)

        val innerLayout: LinearLayout = itemView.findViewById(R.id.cv_scheme)
        init {
            // Set OnClickListener for innerLayout view
            innerLayout.setOnClickListener {
                // Handle click event here
                val intent = Intent(itemView.context, GetPdf::class.java)
                intent.putExtra("id",ttbook.text)
                itemView.context.startActivity(intent)
            }
        }

    }

}
