package com.example.mercury_task1

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.color_items_card.view.*

class RecyclerAdapter(var items: ArrayList<ColorItem>, val t: String) : RecyclerView.Adapter<RecyclerAdapter.Itemholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Itemholder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.color_items_card, parent, false)
        return Itemholder(view)
    }
    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Itemholder, position: Int) {
        val curItem = items[position]
        holder.imageView.setImageBitmap(curItem.imgResource)
        holder.imageView.colorFilter = PorterDuffColorFilter(curItem.color, PorterDuff.Mode.SRC_IN)
        holder.textView.text = items[position].label
    }
    inner class Itemholder(v: View):RecyclerView.ViewHolder(v), View.OnClickListener{

        var imageView: ImageView = v.imgView
        var textView: TextView = v.text1

        override fun onClick(view: View?) {
            if (view != null) {
                Snackbar.make(view, t + " " + textView.text, Snackbar.LENGTH_SHORT).show()
            }
        }

        init {
            v.setOnClickListener(this)
        }
    }
}
