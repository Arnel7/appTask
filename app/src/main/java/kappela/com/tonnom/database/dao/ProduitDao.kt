package kappela.com.tonnom.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kappela.com.tonnom.database.entities.Produit
import kotlinx.coroutines.flow.Flow

@Dao
interface ProduitDao {
    @Query("SELECT * FROM produits ORDER BY nom ASC")
    fun getTousProduits(): Flow<List<Produit>>

    @Insert
    suspend fun ajouterProduit(produit: Produit)

    @Delete
    suspend fun supprimerProduit(produit: Produit)

    @Query("SELECT * FROM produits WHERE stock < :seuilStock")
    suspend fun getProduitsStockFaible(seuilStock: Int): List<Produit>
}