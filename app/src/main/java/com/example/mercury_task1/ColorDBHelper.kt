package com.example.mercury_task1

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ColorDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_COLOR_DB)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DELETE_COLOR_DB)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "app.db"
        const val DB_COLOR_TABLE_NAME: String = "colors"
        const val DB_COL_ID: String = "Id"
        const val DB_COL_COLOR: String = "Color"
        const val DB_COL_VISIBLE: String = "Visible"
        private const val PRIM_KEY: String = "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
        private val CREATE_COLOR_DB: String =
            "CREATE TABLE IF NOT EXISTS $DB_COLOR_TABLE_NAME($DB_COL_ID $PRIM_KEY, $DB_COL_COLOR INTEGER NOT NULL, $DB_COL_VISIBLE INTEGER NOT NULL)";
         val DELETE_COLOR_DB: String = "DROP TABLE IF EXISTS $DB_COLOR_TABLE_NAME"
        fun checkIfAlreadyExists(db: SQLiteDatabase, tableName: String = DB_COLOR_TABLE_NAME): Boolean{
            return db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$tableName' COLLATE NOCASE", null) != null
        }
    }
}
