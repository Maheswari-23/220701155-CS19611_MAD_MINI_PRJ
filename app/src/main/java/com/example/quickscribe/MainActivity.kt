package com.example.quickscribe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var etNote: EditText
    private lateinit var btnAddNote: Button
    private lateinit var btnToggleDarkMode: ImageButton
    private lateinit var btnAddImage: ImageButton
    private lateinit var btnAddVoice: ImageButton
    private lateinit var lvNotes: ListView

    private val notesList = ArrayList<Note>()
    private lateinit var noteAdapter: NoteAdapter
    private var editingNoteId: Long = -1 // -1 means we're adding new note, not editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        searchView = findViewById(R.id.searchView)
        etNote = findViewById(R.id.etNote)
        btnAddNote = findViewById(R.id.btnAddNote)
        btnToggleDarkMode = findViewById(R.id.btnToggleDarkMode)
        btnAddImage = findViewById(R.id.btnAddImage)
        btnAddVoice = findViewById(R.id.btnAddVoice)
        lvNotes = findViewById(R.id.lvNotes)

        // Setup adapter
        noteAdapter = NoteAdapter(this, notesList,
            onEditClicked = { note ->
                startEditingNote(note)
            },
            onDeleteClicked = { position ->
                deleteNote(position)
            }
        )
        lvNotes.adapter = noteAdapter

        // Add Note button click listener
        btnAddNote.setOnClickListener {
            if (editingNoteId == -1L) {
                addNewNote()
            } else {
                updateExistingNote()
            }
        }

        // Dark mode toggle
        btnToggleDarkMode.setOnClickListener {
            toggleDarkMode()
        }

        // Add image feature (placeholder)
        btnAddImage.setOnClickListener {
            Toast.makeText(this, "Add image feature coming soon", Toast.LENGTH_SHORT).show()
        }

        // Add voice note feature (placeholder)
        btnAddVoice.setOnClickListener {
            Toast.makeText(this, "Voice notes feature coming soon", Toast.LENGTH_SHORT).show()
        }

        // Setup search
        setupSearch()

        // Setup note item click listeners
        setupNoteItemClickListeners()
    }

    private fun addNewNote() {
        val noteText = etNote.text.toString().trim()
        if (noteText.isNotEmpty()) {
            // Get current date and time
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val currentDate = sdf.format(Date())

            // Create new note
            val note = Note(
                id = System.currentTimeMillis(),
                content = noteText,
                dateCreated = currentDate
            )

            // Add to list and update adapter
            notesList.add(0, note) // Add to top of list
            noteAdapter.notifyDataSetChanged()

            // Clear input field
            etNote.text.clear()

            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startEditingNote(note: Note) {
        // Set the note content to edit text
        etNote.setText(note.content)
        // Save the ID of the note being edited
        editingNoteId = note.id
        // Change button text to indicate editing
        btnAddNote.text = "Update Note"
        // Focus on the edit text
        etNote.requestFocus()
    }

    private fun updateExistingNote() {
        val updatedText = etNote.text.toString().trim()
        if (updatedText.isNotEmpty()) {
            // Find the note in the list
            val noteIndex = notesList.indexOfFirst { it.id == editingNoteId }
            if (noteIndex != -1) {
                // Update the note
                val currentNote = notesList[noteIndex]
                val updatedNote = Note(
                    id = currentNote.id,
                    content = updatedText,
                    dateCreated = currentNote.dateCreated + " (edited)",
                    imagePath = currentNote.imagePath,
                    voicePath = currentNote.voicePath
                )
                notesList[noteIndex] = updatedNote
                noteAdapter.notifyDataSetChanged()

                // Reset editing state
                editingNoteId = -1L
                etNote.text.clear()
                btnAddNote.text = "Add Note"

                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteNote(position: Int) {
        notesList.removeAt(position)
        noteAdapter.notifyDataSetChanged()

        // If we were editing the note that was deleted, reset the editing state
        if (position < notesList.size && notesList[position].id == editingNoteId) {
            editingNoteId = -1L
            etNote.text.clear()
            btnAddNote.text = "Add Note"
        }

        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
    }

    private fun toggleDarkMode() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        // Recreate activity to apply theme
        recreate()
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText)
                return true
            }
        })
    }

    private fun filterNotes(query: String?) {
        if (query.isNullOrBlank()) {
            noteAdapter = NoteAdapter(this, notesList,
                onEditClicked = { note -> startEditingNote(note) },
                onDeleteClicked = { position -> deleteNote(position) }
            )
        } else {
            val filteredList = notesList.filter { note ->
                note.content.contains(query, ignoreCase = true)
            }
            noteAdapter = NoteAdapter(this, ArrayList(filteredList),
                onEditClicked = { note -> startEditingNote(note) },
                onDeleteClicked = { position -> deleteNote(position) }
            )
        }
        lvNotes.adapter = noteAdapter
    }

    private fun setupNoteItemClickListeners() {
        lvNotes.setOnItemClickListener { _, _, position, _ ->
            val note = noteAdapter.getItem(position) as Note
            startEditingNote(note)
        }
    }
}