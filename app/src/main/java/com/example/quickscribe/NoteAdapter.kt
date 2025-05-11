package com.example.quickscribe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

class NoteAdapter(
    context: Context,
    private val notes: ArrayList<Note>,
    private val onEditClicked: (Note) -> Unit,
    private val onDeleteClicked: (Int) -> Unit
) : ArrayAdapter<Note>(context, 0, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val note = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_note, parent, false)

        // Lookup view for data population
        val tvNoteContent = view.findViewById<TextView>(R.id.tvNoteContent)
        val tvNoteDate = view.findViewById<TextView>(R.id.tvNoteDate)
        val btnEditNote = view.findViewById<ImageButton>(R.id.btnEditNote)
        val btnDeleteNote = view.findViewById<ImageButton>(R.id.btnDeleteNote)

        // Populate the data into the template view
        note?.let {
            tvNoteContent.text = it.content
            tvNoteDate.text = it.dateCreated

            // Edit button click listener
            btnEditNote.setOnClickListener {
                onEditClicked(note)
            }

            // Delete button click listener
            btnDeleteNote.setOnClickListener {
                onDeleteClicked(position)
            }
        }

        return view
    }
}