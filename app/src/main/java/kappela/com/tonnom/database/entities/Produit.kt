package kappela.com.tonnom.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produits")
data class Produit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String,
    val prix: Double,
    val stock: Int
)