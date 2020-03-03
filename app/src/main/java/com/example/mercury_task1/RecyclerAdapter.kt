package com.example.mercury_task1

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.color_items_card.view.*

class RecyclerAdapter(private val items: ArrayList<ColorItem>, private val callbackFun:(str: String)->Unit ) : RecyclerView.Adapter<RecyclerAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.color_items_card, parent, false)
        return ItemHolder(view)
    }
    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        if(position % 8 == 0){
            return 1;
        }
        else return 0
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val curItem = items[position]
        if(getItemViewType(position) == 1) {
            holder.imageView.visibility = View.INVISIBLE
        }
        else holder.imageView.drawable.mutate().colorFilter = PorterDuffColorFilter(curItem.color, PorterDuff.Mode.SRC_IN);
        holder.textView.text = items[position].label
    }
    inner class ItemHolder(v: View):RecyclerView.ViewHolder(v), View.OnClickListener{

        var imageView: ImageView = v.cardImageView
        var textView: TextView = v.cardText

        override fun onClick(view: View?) = callbackFun(textView.text.toString())

        init {
            v.setOnClickListener(this)
        }
    }
}
