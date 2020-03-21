package com.example.mercury_task1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ColorListActivity : AppCompatActivity() {

    private lateinit var coloredItemsRecyclerView: RecyclerView
    private lateinit var itemsList: ArrayList<ColorItem>

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
            if (ColorTableDAO.checkIfTableExists(this@ColorListActivity)) {
                println("DB already exists")
                itemsList = ColorTableDAO.getAdapterItemsFromDB(this@ColorListActivity)
            } else {
                println("Creating DB")
                itemsList = ColorTableDAO.createDatabase(this@ColorListActivity)
            }
            runOnUiThread {
                coloredItemsRecyclerView.adapter = RecyclerAdapter(itemsList) { str ->
                    Snackbar.make(
                        coloredItemsRecyclerView,
                        getString(R.string.item_clicked, str),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        itemsFromDB.start()

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
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
                            itemsList.removeAt(pos)
                            runOnUiThread {
                                coloredItemsRecyclerView.adapter!!.notifyDataSetChanged()
                            }
                        }
                        thread.start()
                    }
                    deleteDialog.setNegativeButton(android.R.string.no) { _, _ ->
                        coloredItemsRecyclerView.adapter!!.notifyItemChanged(pos)
                    }
                    deleteDialog.show()
                }

            })
        itemTouchHelper.attachToRecyclerView(coloredItemsRecyclerView)

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
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
}
