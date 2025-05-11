package com.example.quickscribe

data class Note(
    val id: Long,
    val content: String,
    val dateCreated: String,
    val imagePath: String? = null,
    val voicePath: String? = null
)