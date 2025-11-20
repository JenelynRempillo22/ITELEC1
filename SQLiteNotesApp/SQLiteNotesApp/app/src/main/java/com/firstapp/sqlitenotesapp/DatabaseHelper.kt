package com.firstapp.sqlitenotesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyNotes.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NOTES = "notes"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"

        private const val SQL_CREATE_TABLE_NOTES =
            "CREATE TABLE $TABLE_NOTES (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT NOT NULL," +
                    "$COLUMN_CONTENT TEXT);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun insertNote(title: String, content: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
        }
        val newRowId = db.insert(TABLE_NOTES, null, values)
        db.close()
        return newRowId
    }

    fun getAllNotes(): List<String> {
        val notesList = mutableListOf<String>()
        val selectQuery = "SELECT * FROM $TABLE_NOTES"
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                val note = "[$id] $title - $content"
                notesList.add(note)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return notesList
    }

    // Delete a note by ID
    fun deleteNote(id: Int): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_NOTES, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted
    }

    // Helper method to extract ID from the display string
    fun extractNoteId(noteString: String): Int? {
        val regex = """\[(\d+)\]""".toRegex()
        val matchResult = regex.find(noteString)
        return matchResult?.groupValues?.get(1)?.toIntOrNull()
    }
}