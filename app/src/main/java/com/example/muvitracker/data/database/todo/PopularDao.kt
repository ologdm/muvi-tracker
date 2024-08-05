package com.example.muvitracker.data.database.todo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.database.entities.PopularMovieEntity
import kotlinx.coroutines.flow.Flow

// room + paging

@Dao
interface PopularDao {

    // !! aggiorna tutti gli elemeni della chiamata internet,
    // ad ogni chiamata elementi aggiornati di nuovo

    // insert or update
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<PopularMovieEntity>)

    // read
    @Query("SELECT * FROM PopularEntities")
    fun readAll() : Flow<List<PopularMovieEntity>>

}

