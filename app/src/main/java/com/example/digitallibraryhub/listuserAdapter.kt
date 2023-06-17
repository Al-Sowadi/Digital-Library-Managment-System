package com.example.digitallibraryhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class listuserAdapter(private val userList: ArrayList<userclase>) : RecyclerView.Adapter<listuserAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listuserAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.userlayput,parent,false)
        return listuserAdapter.MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder:listuserAdapter.MyViewHolder, position: Int) {
        val users1 :userclase=userList[position]
        holder.seminarname1.text=users1.u1
        holder.seminartime2.text=users1.u2
    }
    override fun getItemCount(): Int {
        return userList.size
    }
    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val seminarname1: TextView =itemView.findViewById(R.id.idTVCourseName)
        val seminartime2: TextView =itemView.findViewById(R.id.idTVCourseRating)
    }
}