package com.example.mercury_task1

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        coloredItemsRecyclerView.adapter = RecyclerAdapter(itemsList, getString(R.string.item_clicked))

    }

    private fun fillAdapterItems(): ArrayList<ColorItem>{
        val list = ArrayList<ColorItem>()
        val d: Drawable = getDrawable(R.drawable.ic_circle)!!
        for (i in 0 until ELEMENTS_NUM){
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
            val b: Bitmap = convertToBitmap(d)
            val item = ColorItem(b, c,  "${getString(R.string.item_text)} ${i+1}")
            list += item
        }
        return list
    }

    private fun convertToBitmap(d: Drawable): Bitmap{
        val b: Bitmap = Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(b)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        return b
    }
}
