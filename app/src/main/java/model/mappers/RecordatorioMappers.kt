package com.example.prueba.model.mappers


import com.example.prueba.data.local.RecordatorioEntity
import com.example.prueba.model.Recordatorio

fun RecordatorioEntity.toDto() = Recordatorio(
    id = id,
    uid = uid,
    createdAt = createdAt,
    message = message
)

fun Recordatorio.toEntity() = RecordatorioEntity(
    id = id,
    uid = uid,
    createdAt = createdAt,
    message = message
)