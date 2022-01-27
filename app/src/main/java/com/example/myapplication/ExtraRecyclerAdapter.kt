package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

interface onItemClickListener{
    fun OnItemClick(position: Int)
}

class ExtraRecyclerAdapter(val itemLi: ArrayList<ExtraItem>, private val listener: onItemClickListener): RecyclerView.Adapter<ExtraRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var itemTextRu: TextView
        var itemTextEng: TextView
        init {
            //itemView.setOnClickListener(this)
            itemTextEng = itemView.findViewById(R.id.engW)
            itemTextRu = itemView.findViewById(R.id.ruW)
            itemView.setOnClickListener(this)

        }

        override fun onClick(p0: View?) {
            listener.OnItemClick(adapterPosition)
        }

        //override fun onClick(view: View?) {
        //    if(adapterPosition != RecyclerView.NO_POSITION){
        //        listener.OnItemClick(adapterPosition)
        //    }
//
        //}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dict_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemLi[position]
        holder.itemTextEng.text = currentItem.engWord
        holder.itemTextRu.text = currentItem.ruWord
    }

    override fun getItemCount(): Int {
        return itemLi.size
    }

}