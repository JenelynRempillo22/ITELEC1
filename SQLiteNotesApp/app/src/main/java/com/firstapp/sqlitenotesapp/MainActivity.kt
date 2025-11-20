package com.firstapp.sqlitenotesapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button
    private lateinit var listNotes: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        etTitle = findViewById(R.id.et_title)
        etContent = findViewById(R.id.et_content)
        btnSave = findViewById(R.id.btn_save)
        listNotes = findViewById(R.id.list_notes)

        dbHelper = DatabaseHelper(this)

        btnSave.setOnClickListener { saveNote() }

        // Long-click listener to delete notes
        listNotes.setOnItemLongClickListener { parent, view, position, id ->
            val noteString = parent.getItemAtPosition(position) as String
            showDeleteConfirmation(noteString)
            true
        }

        loadNotes()
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        val id = dbHelper.insertNote(title, content)
        if (id != -1L) {
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            etTitle.setText("")
            etContent.setText("")
            loadNotes()
        } else {
            Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadNotes() {
        val notes = dbHelper.getAllNotes()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        listNotes.adapter = adapter
    }

    private fun showDeleteConfirmation(noteString: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteNote(noteString)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteNote(noteString: String) {
        val noteId = dbHelper.extractNoteId(noteString)

        if (noteId != null) {
            val rowsDeleted = dbHelper.deleteNote(noteId)
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
                loadNotes()
            } else {
                Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error: Could not identify note", Toast.LENGTH_SHORT).show()
        }
    }
}