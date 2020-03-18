package com.example.mercury_task1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class ColorDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_COLOR_DB)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_COLOR_DB)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "app.db"
        const val DB_COLOR_TABLE_NAME: String = "colors"
        const val DB_COL_ID: String = "Id"
        const val DB_COL_COLOR: String = "Color"
        const val DB_COL_VISIBLE: String = "Visible"
        private const val PRIM_KEY: String = "INTEGER PRIMARY KEY NOT NULL"
        const val CREATE_COLOR_DB: String =
            "CREATE TABLE IF NOT EXISTS $DB_COLOR_TABLE_NAME($DB_COL_ID $PRIM_KEY, $DB_COL_COLOR INTEGER NOT NULL, $DB_COL_VISIBLE INTEGER NOT NULL)";
        const val DELETE_COLOR_DB: String = "DROP TABLE IF EXISTS $DB_COLOR_TABLE_NAME"
        fun checkIfAlreadyExists(
            db: SQLiteDatabase,
            tableName: String = DB_COLOR_TABLE_NAME
        ): Boolean {
            try {
                val cursor = db.query(
                    tableName, null,
                    null, null, null, null, null
                )
                return true
            } catch (e: Exception) {
                return false
            }
        }

        fun idList(db: SQLiteDatabase, tableName: String = DB_COLOR_TABLE_NAME){
            println("IDs list:")
            val cursor = db.rawQuery("SELECT $DB_COL_ID FROM $tableName", null)
            with(cursor) {
                if (moveToFirst()) {
                    println("ID: ${getInt(getColumnIndexOrThrow(DB_COL_ID))}")
                }
            }
        }

        fun getMaxId(db: SQLiteDatabase, tableName: String = DB_COLOR_TABLE_NAME): Int {
            val cursor = db.rawQuery("SELECT MAX($DB_COL_ID) as $DB_COL_ID FROM $tableName", null)
            var max = 0
            with(cursor) {
                if(moveToFirst()) {
                    max = getInt(getColumnIndexOrThrow(DB_COL_ID))
                }
            }
            println("Max ID: $max")
            return max
        }
    }
}
