package com.example.mercury_task1

import android.content.ContentValues
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

class CreateElementActivity : AppCompatActivity() {

    private lateinit var itemName: String
    private var itemColor: Int = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_element)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val db = ColorDBHelper(this).writableDatabase
        itemName = getString(R.string.item_text, ColorDBHelper.getMaxId(db) + 1)
        this.title = itemName
        val colorButton = findViewById<Button>(R.id.colorButton)
        val createElementButton = findViewById<Button>(R.id.createElementButton)
        colorButton.background.colorFilter =
            PorterDuffColorFilter(itemColor, PorterDuff.Mode.SRC_IN)
        colorButton.setOnClickListener { button ->
            val colorPicker = ColorPicker(this, 127, 127, 127)
            colorPicker.show()
            colorPicker.setCallback { color ->
                itemColor = color
                button.background.colorFilter =
                    PorterDuffColorFilter(itemColor, PorterDuff.Mode.SRC_IN)
                colorPicker.dismiss()
            }
        }
        createElementButton.setOnClickListener {
            val values = ContentValues().apply {
                put(ColorDBHelper.DB_COL_COLOR, itemColor)
                put(ColorDBHelper.DB_COL_VISIBLE, 1)
            }
            ColorListActivity.itemsList.add(ColorItem(itemColor, itemName, true))
            db.insert(ColorDBHelper.DB_COLOR_TABLE_NAME, null, values)
            onSupportNavigateUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
