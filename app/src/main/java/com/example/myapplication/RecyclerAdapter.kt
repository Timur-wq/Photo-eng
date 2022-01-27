package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val itemList: MutableList<ListItem>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    lateinit var myListener: RecyclerViewClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cards, parent, false)
        return ViewHolder(v, myListener)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.ItemImage.setImageBitmap(currentItem.imageItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View, listener: RecyclerViewClickListener): RecyclerView.ViewHolder(itemView){
        var ItemImage: ImageView
        var button: Button
        var textView: TextView
        init {
            ItemImage = itemView.findViewById(R.id.imView)
            button = itemView.findViewById(R.id.btn)
            button.setOnClickListener {
                listener.onClick(it, adapterPosition)
            }

            textView = itemView.findViewById(R.id.date)
        }
    }

    interface RecyclerViewClickListener{
        fun onClick(v: View, postion: Int){

        }
    }

    fun setOnItemClickListener(listener: RecyclerViewClickListener){
        myListener = listener
    }
}