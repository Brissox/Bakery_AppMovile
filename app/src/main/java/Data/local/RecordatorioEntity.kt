package com.example.prueba.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recordatorios")
data class RecordatorioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val uid: String,
    val createdAt: String,
    val message: String
)