package com.example.warehouse.storekeeper.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val orderTypeId: Int
) {
    val typeName: String
        get() = when (orderTypeId) {
            1 -> "Поступление"
            2 -> "Отгрузка"

            else -> "Неизвестный тип"
        }
}
