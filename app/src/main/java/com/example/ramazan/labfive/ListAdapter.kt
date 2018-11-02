package com.example.ramazan.labfive

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.post_item.view.*

class ListAdapter(var posts: List<Post>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = posts[position].title
        holder.body.text = posts[position].body
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var title: TextView = itemView!!.title
        var body: TextView = itemView!!.body


    }
}