package com.example.lavanderia_cliente.database.dao;

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lavanderia_cliente.model.PecaRoupa

@Dao
//abstract class PecaRoupaDaoRoom: BaseDAO<PecaRoupa>
abstract class PecaRoupaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun salva(pecaRoupa: PecaRoupa): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun salvaVarias(pecaRoupa: List<PecaRoupa>)

    @Delete
    abstract fun remove(vararg pecaRoupa: PecaRoupa?)

    @Query(
        "DELETE FROM PecaRoupa " +
                "WHERE PecaRoupa.id = :id"
    )
    abstract fun deletePorId(id: Long?): Int

    @Update()
    abstract fun edita(vararg peca: PecaRoupa?): Int

    @Query("SELECT * FROM PecaRoupa")
    abstract fun todas(): LiveData<List<PecaRoupa>?>

}
