package com.example.mercury_task1

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ColorListActivity : AppCompatActivity() {
    private lateinit var coloredItemsRecyclerView: RecyclerView

    private val ELEMENTS_NUM: Int = 50
    private val COLORS_NUM: Int = 8

    private val PRIM_KEY: String = "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
    private val CREATE_COLOR_DB: String =
        "CREATE TABLE IF NOT EXISTS colors(Id $PRIM_KEY, Color INTEGER NOT NULL, Visible INTEGER NOT NULL)";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_list)
        coloredItemsRecyclerView = findViewById(R.id.coloredItemsRecyclerView)
        coloredItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        coloredItemsRecyclerView.setHasFixedSize(true)

        val db: SQLiteDatabase = baseContext.openOrCreateDatabase("app.db", Context.MODE_PRIVATE, null)
        val itemsList: ArrayList<ColorItem>
        if(ColorDBHelper.checkIfAlreadyExists(db)){
            itemsList = getAdapterItemsFromDB()
        }
        else{
            db.execSQL(CREATE_COLOR_DB)
            itemsList = fillAdapterItems()
        }

        coloredItemsRecyclerView.adapter = RecyclerAdapter(itemsList) { str ->
            Snackbar.make(coloredItemsRecyclerView, getString(R.string.item_clicked, str), Snackbar.LENGTH_SHORT).show()
        }
        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener { view ->
            val intent: Intent = Intent(this, CreateElementActivity::class.java)
            startActivity(intent)
            Snackbar.make(view, "Add button pressed", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun fillAdapterItems(): ArrayList<ColorItem>{
        val list = ArrayList<ColorItem>()
        val dbWritable = ColorDBHelper(this).writableDatabase
        for (i in 0 until ELEMENTS_NUM){
            val c = when (i % COLORS_NUM){
                0 -> Color.RED
                1 -> Color.rgb(255, 165, 0)
                2 -> Color.YELLOW
                3 -> Color.GREEN
                4 -> Color.rgb(66, 170, 255)
                5 -> Color.BLUE
                6 -> Color.rgb(139, 0 ,255)
                else -> 0
            }
            var visible: Boolean = when (i % COLORS_NUM) {
                7 -> false
                else -> true
            }
            val values = ContentValues().apply {
                put(ColorDBHelper.DB_COL_COLOR, c)
                put(ColorDBHelper.DB_COL_VISIBLE, (if (visible){1} else{0}))
            }
            println(dbWritable.insert(ColorDBHelper.DB_COLOR_TABLE_NAME, null, values))
            val item = ColorItem(c, getString(R.string.item_text, i+1), visible)
            list += item
        }
        return list
    }

    private fun getAdapterItemsFromDB(): ArrayList<ColorItem>{
        val list = ArrayList<ColorItem>()
        val dbReadable = ColorDBHelper(this).readableDatabase
        val projection = arrayOf(ColorDBHelper.DB_COL_ID, ColorDBHelper.DB_COL_COLOR, ColorDBHelper.DB_COL_VISIBLE)
        val sortOrder = "${ColorDBHelper.DB_COL_ID} ASC"
        val cursor = dbReadable.query(
            ColorDBHelper.DB_COLOR_TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_ID))
                val itemColor = getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_COLOR))
                val itemVisible = getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_VISIBLE)) == 1
                list += ColorItem(itemColor, getString(R.string.item_text, itemId), itemVisible)
                //println("$itemId, $itemColor, $itemVisible")
            }
        }
        return list
    }
}
