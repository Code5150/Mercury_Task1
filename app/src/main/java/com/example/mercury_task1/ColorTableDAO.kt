package com.example.mercury_task1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class ColorTableDAO() {
    companion object {
        fun putColorItemIntoDB(context: Context, item: ColorItem) {
            val db = ColorDBHelper(context).writableDatabase
            db.enableWriteAheadLogging()
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
            db.insert(ColorDBHelper.DB_COLOR_TABLE_NAME, null, values)
            db.close()
        }

        fun deleteColorItemFromDB(context: Context, id: String) {
            val db = ColorDBHelper(context).writableDatabase
            db.enableWriteAheadLogging()
            val selection = "${ColorDBHelper.DB_COL_ID} = ?"
            val selectionArgs = arrayOf(id)
            db.delete(
                ColorDBHelper.DB_COLOR_TABLE_NAME,
                selection,
                selectionArgs
            )
            db.close()
        }

        fun getAdapterItemsFromDB(context: Context): ArrayList<ColorItem> {
            val list = ArrayList<ColorItem>()
            val db = ColorDBHelper(context).readableDatabase
            db.enableWriteAheadLogging()
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
                    val itemColor = getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_COLOR))
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

        fun getMaxId(context: Context): Int {
            val db = ColorDBHelper(context).readableDatabase
            db.enableWriteAheadLogging()
            val cursor = db.rawQuery("SELECT MAX(${ColorDBHelper.DB_COL_ID}) as ${ColorDBHelper.DB_COL_ID} FROM ${ColorDBHelper.DB_COLOR_TABLE_NAME}", null)
            var max = 0
            with(cursor) {
                if(moveToFirst()) {
                    max = getInt(getColumnIndexOrThrow(ColorDBHelper.DB_COL_ID))
                }
            }
            cursor.close()
            db.close()
            println("Max ID: $max")
            return max
        }

        fun checkIfTableExists(context: Context): Boolean{
            val db = ColorDBHelper(context).readableDatabase
            db.enableWriteAheadLogging()
            val cursor: Cursor? = db.query(
                ColorDBHelper.DB_COLOR_TABLE_NAME, null,
                null, null, null, null, null
            )
            println(cursor)
            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    cursor.close()
                    db.close()
                    return true
                }
                else{
                    cursor.close()
                    db.close()
                    return false
                }
            } else {
                db.close()
                return false
            }
        }

        fun createDatabase(context: Context){
            val db = ColorDBHelper(context).writableDatabase
            db.execSQL(ColorDBHelper.CREATE_COLOR_DB)
            db.close()
        }
    }
}