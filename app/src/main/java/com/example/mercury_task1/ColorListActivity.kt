package com.example.mercury_task1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class ColorListActivity : AppCompatActivity(){

    private lateinit var coloredItemsRecyclerView: RecyclerView
    private lateinit var itemsList: ArrayList<ColorItem>

    private val ELEMENTS_NUM: Int = 50
    private val COLORS_NUM: Int = 8
    private val CODE_RESULT: Int = 1

    companion object {
        const val ITEM_RESULT: String = "ITEM_RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_list)
        coloredItemsRecyclerView = findViewById(R.id.coloredItemsRecyclerView)
        coloredItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        coloredItemsRecyclerView.setHasFixedSize(true)

        val itemsFromDB = Thread {
            if (ColorTableDAO.checkIfExistsDB(baseContext)) {
                itemsList = ColorTableDAO.getAdapterItemsFromDB(this@ColorListActivity)
            } else {
                itemsList = fillAdapterItems()
            }
        }
        itemsFromDB.start()

        itemsFromDB.join()
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
                        val thread = Thread {
                            ColorTableDAO.deleteColorItemFromDB(
                                this@ColorListActivity,
                                itemsList[pos].label.filter { it.isDigit() })
                        }
                        thread.start()
                        thread.join()
                        itemsList.removeAt(pos)
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
            startActivityForResult(intent, CODE_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    itemsList.add(
                        itemsList.size,
                        data.getParcelableExtra(ColorListActivity.ITEM_RESULT)
                    )
                    coloredItemsRecyclerView.adapter!!.notifyItemInserted(itemsList.size - 1)
                    coloredItemsRecyclerView.scrollToPosition(itemsList.size - 1)
                }
            }
        }
    }

    private fun fillAdapterItems(): ArrayList<ColorItem> {
        val list = ArrayList<ColorItem>()
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
            val item = ColorItem(c, getString(R.string.item_text, i + 1), visible)
            ColorTableDAO.putColorItemIntoDB(this@ColorListActivity, item)
            list += item
        }
        return list
    }
}
