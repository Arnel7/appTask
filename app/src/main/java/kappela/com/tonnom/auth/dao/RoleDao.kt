package kappela.com.tonnom.auth.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kappela.com.tonnom.auth.entities.Role

@Dao
interface RoleDao {

    @Query("SELECT * FROM roles")
    suspend fun getTousRoles(): List<Role>

    @Query("SELECT * FROM roles WHERE nom = :nom")
    suspend fun getRoleParNom(nom: String): Role?

    @Query("SELECT * FROM roles WHERE id = :id")
    suspend fun getRoleParId(id: Int): Role?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun ajouterRole(role: Role)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun ajouterRoles(roles: List<Role>)
}