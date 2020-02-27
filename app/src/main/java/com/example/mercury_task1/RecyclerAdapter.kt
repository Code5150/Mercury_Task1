package com.example.mercury_task1

import android.app.Application
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.MotionEvent
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

        holder.imageView.setImageResource(currentItem.imgResource)
        val d: Drawable = holder.imageView.drawable
        d.colorFilter = PorterDuffColorFilter(currentItem.color, PorterDuff.Mode.SRC_IN)
        holder.textView.text = currentItem.label

    }
    inner class Itemholder(private var v: View, var t: String):RecyclerView.ViewHolder(v), View.OnClickListener, View.OnTouchListener{

        val imageView: ImageView = v.imgView
        val textView: TextView = v.text1


        init {

            v.setOnClickListener(this)
            v.setOnTouchListener(this)
        }


        override fun onClick(view: View?) {
            if (view != null) {
                //view.startAnimation(AlphaAnimation(1F, 0.8F))
                Snackbar.make(view, t + " " + textView.text, Snackbar.LENGTH_SHORT).show()
            }
        }

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {

            if (view != null) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onClick(view)
                        view.text1.setTextColor(Color.rgb(255,255,255))
                        view.background.colorFilter = PorterDuffColorFilter(Color.argb(71, 0, 0, 0), PorterDuff.Mode.SRC_ATOP)
                        v.invalidate()
                    }
                    MotionEvent.ACTION_UP -> {
                        view.text1.setTextColor(Color.rgb(141,141,141))
                        view.background.clearColorFilter()
                        v.invalidate()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        view.text1.setTextColor(Color.rgb(141,141,141))
                        view.background.clearColorFilter()
                        v.invalidate()
                    }
                }

            }

            return true
        }

    }

}
