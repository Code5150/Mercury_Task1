package com.example.mercury_task1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.color_items_card.view.*

class RecyclerAdapter(private var items: ArrayList<ColorItem>, val clickText: String) : RecyclerView.Adapter<RecyclerAdapter.Itemholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Itemholder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.color_items_card, parent, false)
        return Itemholder(view, clickText)
    }
    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Itemholder, position: Int) {
        val currentItem = items[position]

        holder.imageView.setImageDrawable(currentItem.imgResource)

        holder.textView.text = currentItem.label

    }
    inner class Itemholder(private var v: View, var t: String):RecyclerView.ViewHolder(v), View.OnClickListener{

        val imageView: ImageView = v.imgView
        val textView: TextView = v.text1

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (view != null) {
                Snackbar.make(view, t + " " + textView.text, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
