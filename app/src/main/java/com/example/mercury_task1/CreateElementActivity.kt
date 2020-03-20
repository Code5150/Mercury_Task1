package com.example.mercury_task1

import android.app.Activity
import android.content.Intent
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
        val thread = Thread {
            itemName = getString(
                R.string.item_text,
                ColorTableDAO.getMaxId(this@CreateElementActivity) + 1
            )
            runOnUiThread {
                this@CreateElementActivity.title = itemName
            }
        }
        thread.start()
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
            val returnIntent = Intent()
            val resultItem = ColorItem(itemColor, itemName, true)
            returnIntent.putExtra(ColorListActivity.ITEM_RESULT, resultItem)
            setResult(Activity.RESULT_OK, returnIntent)
            val thread = Thread {
                ColorTableDAO.putColorItemIntoDB(this@CreateElementActivity, resultItem)
            }
            thread.start()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
