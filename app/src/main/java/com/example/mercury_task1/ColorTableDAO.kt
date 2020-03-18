package com.example.mercury_task1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class ColorTableDAO() {
    companion object {
        fun putColorItemIntoDB(context: Context, item: ColorItem) {
            val db = ColorDBHelper(context).writableDatabase
            val values = ContentValues().apply {
                put(ColorDBHelper.DB_COL_ID, ColorDBHelper.getMaxId(db) + 1)
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

        fun getMaxId(context: Context): Int {
            val db = ColorDBHelper(context).writableDatabase
            val result = ColorDBHelper.getMaxId(db)
            db.close()
            return result
        }

        fun deleteColorItemFromDB(context: Context, id: String) {
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

        fun getAdapterItemsFromDB(context: Context): ArrayList<ColorItem> {
            val list = ArrayList<ColorItem>()
            val dbReadable = ColorDBHelper(context).readableDatabase
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
            dbReadable.close()
            return list
        }

        fun idList(context: Context){
            val dbReadable = ColorDBHelper(context).readableDatabase
            ColorDBHelper.idList(dbReadable)
            dbReadable.close()
        }

        fun checkIfExistsDB(baseContext: Context): Boolean {
            val db: SQLiteDatabase =
                baseContext.openOrCreateDatabase("app.db", Context.MODE_PRIVATE, null)
            db.execSQL(ColorDBHelper.DELETE_COLOR_DB)
            if (ColorDBHelper.checkIfAlreadyExists(db)) {
                db.close()
                return true
            } else {
                db.execSQL(ColorDBHelper.CREATE_COLOR_DB)
                db.close()
                return false
            }
        }
    }
}