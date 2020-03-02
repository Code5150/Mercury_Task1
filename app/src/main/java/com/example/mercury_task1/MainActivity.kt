package com.example.mercury_task1

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var coloredItemsRecyclerView: RecyclerView

    private val ELEMENTS_NUM: Int = 50
    private val COLORS_NUM: Int = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coloredItemsRecyclerView = findViewById(R.id.coloredItemsRecyclerView)
        coloredItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        coloredItemsRecyclerView.setHasFixedSize(true)
        val itemsList = fillAdapterItems()
        coloredItemsRecyclerView.adapter = RecyclerAdapter(itemsList) { str -> Unit
            Snackbar.make(coloredItemsRecyclerView, getString(R.string.item_clicked) + " " + str, Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun fillAdapterItems(): ArrayList<ColorItem>{
        val list = ArrayList<ColorItem>()

        for (i in 0 until ELEMENTS_NUM){
            //val d: Drawable = getDrawable(R.drawable.ic_circle)!!.mutate()
            val c = when (i % COLORS_NUM){
                0 -> Color.RED
                1 -> Color.rgb(255, 165, 0)
                2 -> Color.YELLOW
                3 -> Color.GREEN
                4 -> Color.rgb(66, 170, 255)
                5 -> Color.BLUE
                6 -> Color.rgb(139, 0 ,255)
                else -> Color.TRANSPARENT
            }
            //d.colorFilter =  PorterDuffColorFilter(c, PorterDuff.Mode.SRC_IN)
            val item = ColorItem(c, getString(R.string.item_text, (i+1)))
            list += item
        }
        return list
    }
}
