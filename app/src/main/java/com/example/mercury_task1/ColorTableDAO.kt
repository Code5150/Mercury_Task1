package com.example.mercury_task1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Color

class ColorTableDAO() {
    companion object {
        fun putColorItemIntoDB(context: Context, item: ColorItem) {
            val values = ContentValues().apply {
                put(ColorDBHelper.DB_COL_ID, getMaxId(context) + 1)
                put(ColorDBHelper.DB_COL_COLOR, item.color)
                put(
                    ColorDBHelper.DB_COL_VISIBLE, (if (item.circleVisible) {
                        1
                    } else {
                        0
                    })
                )
            }
            println(values.toString())
            synchronized(this)
            {
                val db = ColorDBHelper(context).writableDatabase
                db.insert(ColorDBHelper.DB_COLOR_TABLE_NAME, null, values)
                db.close()
            }
        }

        fun deleteColorItemFromDB(context: Context, id: String) {
            synchronized(this) {
                val db = ColorDBHelper(context).writableDatabase
                val selection = "${ColorDBHelper.DB_COL_ID} = ?"
                val selectionArgs = arrayOf(id)
                db.delete(
                    ColorDBHelper.DB_COLOR_TABLE_NAME,
                    selection,
                    selectionArgs
                )
                db.close()
            }
        }

        fun getAdapterItemsFromDB(context: Context): ArrayList<ColorItem> {
            synchronized(this) {
                val list = ArrayList<ColorItem>()
                val db = ColorDBHelper(context).readableDatabase
                val projection = arrayOf(
                    ColorDBHelper.DB_COL_ID,
                    ColorDBHelper.DB_COL_COLOR,
                    ColorDBHelper.DB_COL_VISIBLE
                )
                val sortOrder = "${ColorDBHelper.DB_COL_ID} ASC"
                val cursor = db.query(
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
                        val itemColor =
                            getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_COLOR))
                        val itemVisible: Boolean =
                            getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_VISIBLE)) == 1
                        list += ColorItem(
                            itemColor,
                            context.getString(R.string.item_text, itemId),
                            itemVisible
                        )
                        println("ID: $itemId, Color: $itemColor, Visible: $itemVisible")
                    }
                }
                cursor.close()
                db.close()
                return list
            }
        }

        fun getMaxId(context: Context): Int {
            synchronized(this) {
                val db = ColorDBHelper(context).readableDatabase
                val cursor = db.rawQuery(
                    "SELECT MAX(${ColorDBHelper.DB_COL_ID}) as ${ColorDBHelper.DB_COL_ID} FROM ${ColorDBHelper.DB_COLOR_TABLE_NAME}",
                    null
                )
                var max = 0
                with(cursor) {
                    if (moveToFirst()) {
                        max = getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_ID))
                    }
                }
                cursor.close()
                db.close()
                println("Max ID: $max")
                return max
            }
        }

        fun checkIfTableExists(context: Context): Boolean {
            synchronized(this) {
                val db = ColorDBHelper(context).readableDatabase
                val cursor: Cursor? = db.query(
                    ColorDBHelper.DB_COLOR_TABLE_NAME, null,
                    null, null, null, null, null
                )
                println(cursor)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        cursor.close()
                        db.close()
                        return true
                    } else {
                        cursor.close()
                        db.close()
                        return false
                    }
                } else {
                    db.close()
                    return false
                }
            }
        }

        fun createDatabase(context: Context): ArrayList<ColorItem> {
            val list = ArrayList<ColorItem>()
            synchronized(this) {
                val db = ColorDBHelper(context).writableDatabase
                db.execSQL(ColorDBHelper.CREATE_COLOR_DB)
                for (i in 0 until ColorDBHelper.DEFAULT_ELEMENTS_NUM) {
                    val c = when (i % ColorDBHelper.DEFAULT_COLORS_NUM) {
                        0 -> Color.RED
                        1 -> Color.rgb(255, 165, 0)
                        2 -> Color.YELLOW
                        3 -> Color.GREEN
                        4 -> Color.rgb(66, 170, 255)
                        5 -> Color.BLUE
                        6 -> Color.rgb(139, 0, 255)
                        else -> 0
                    }
                    val visible: Boolean = when (i % ColorDBHelper.DEFAULT_COLORS_NUM) {
                        7 -> false
                        else -> true
                    }
                    list += ColorItem(c, context.getString(R.string.item_text, i + 1), visible)
                    db.insert(ColorDBHelper.DB_COLOR_TABLE_NAME, null, ContentValues().apply {
                        put(ColorDBHelper.DB_COL_ID, i + 1)
                        put(ColorDBHelper.DB_COL_COLOR, c)
                        put(
                            ColorDBHelper.DB_COL_VISIBLE, (if (visible) {
                                1
                            } else {
                                0
                            })
                        )
                    })
                }
                db.close()
            }
            return list
        }
    }
}