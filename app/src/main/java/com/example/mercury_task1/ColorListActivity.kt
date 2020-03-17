package com.example.mercury_task1

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ColorListActivity : AppCompatActivity() {
    private lateinit var coloredItemsRecyclerView: RecyclerView

    private val ELEMENTS_NUM: Int = 50
    private val COLORS_NUM: Int = 8

    companion object {
        lateinit var itemsList: ArrayList<ColorItem>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_list)
        coloredItemsRecyclerView = findViewById(R.id.coloredItemsRecyclerView)
        coloredItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        coloredItemsRecyclerView.setHasFixedSize(true)

        val db: SQLiteDatabase =
            baseContext.openOrCreateDatabase("app.db", Context.MODE_PRIVATE, null)

        if (ColorDBHelper.checkIfAlreadyExists(db)) {
            itemsList = getAdapterItemsFromDB()
        } else {
            db.execSQL(ColorDBHelper.CREATE_COLOR_DB)
            itemsList = fillAdapterItems()
        }
        db.close()

        coloredItemsRecyclerView.adapter = RecyclerAdapter(itemsList) { str ->
            Snackbar.make(
                coloredItemsRecyclerView,
                getString(R.string.item_clicked, str),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val deleteDialog = AlertDialog.Builder(this@ColorListActivity)
                    val pos = viewHolder.adapterPosition
                    deleteDialog.setTitle(R.string.delete_element_title)
                    deleteDialog.setMessage(R.string.delete_element)
                    deleteDialog.setPositiveButton(android.R.string.yes) { _, _ ->
                        val dbIndex: String = itemsList[pos].label.filter { it.isDigit() }
                        itemsList.removeAt(pos)
                        val dbWritable = ColorDBHelper(this@ColorListActivity).writableDatabase
                        val selection = "${ColorDBHelper.DB_COL_ID} LIKE ?"
                        val selectionArgs = arrayOf(dbIndex)
                        dbWritable.delete(
                            ColorDBHelper.DB_COLOR_TABLE_NAME,
                            selection,
                            selectionArgs
                        )
                        (coloredItemsRecyclerView.adapter as RecyclerAdapter).notifyDataSetChanged()
                    }
                    deleteDialog.setNegativeButton(android.R.string.no) { _, _ ->
                        (coloredItemsRecyclerView.adapter as RecyclerAdapter).notifyItemChanged(pos)
                    }
                    deleteDialog.show()
                }

            })
        itemTouchHelper.attachToRecyclerView(coloredItemsRecyclerView)

        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val intent = Intent(this, CreateElementActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        (coloredItemsRecyclerView.adapter as RecyclerAdapter).notifyDataSetChanged()
        super.onResume()
    }

    private fun fillAdapterItems(): ArrayList<ColorItem> {
        val list = ArrayList<ColorItem>()
        val dbWritable = ColorDBHelper(this).writableDatabase
        for (i in 0 until ELEMENTS_NUM) {
            val c = when (i % COLORS_NUM) {
                0 -> Color.RED
                1 -> Color.rgb(255, 165, 0)
                2 -> Color.YELLOW
                3 -> Color.GREEN
                4 -> Color.rgb(66, 170, 255)
                5 -> Color.BLUE
                6 -> Color.rgb(139, 0, 255)
                else -> 0
            }
            var visible: Boolean = when (i % COLORS_NUM) {
                7 -> false
                else -> true
            }
            val values = ContentValues().apply {
                put(ColorDBHelper.DB_COL_COLOR, c)
                put(
                    ColorDBHelper.DB_COL_VISIBLE, (if (visible) {
                        1
                    } else {
                        0
                    })
                )
            }
            dbWritable.insert(ColorDBHelper.DB_COLOR_TABLE_NAME, null, values)
            val item = ColorItem(c, getString(R.string.item_text, i + 1), visible)
            list += item
        }
        return list
    }

    private fun getAdapterItemsFromDB(): ArrayList<ColorItem> {
        val list = ArrayList<ColorItem>()
        val dbReadable = ColorDBHelper(this).readableDatabase
        val projection = arrayOf(
            ColorDBHelper.DB_COL_ID,
            ColorDBHelper.DB_COL_COLOR,
            ColorDBHelper.DB_COL_VISIBLE
        )
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
            }
        }
        return list
    }
}
