package com.example.muvitracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.muvitracker.data.database.entities.SeasonEntity
import kotlinx.coroutines.flow.Flow

// @Update - richiede l'intera entity da aggiornare
// utilizzare episodi totali sempre

// TODO
//  1. read
//  2. insert
//  3.1 update dto
//  3.2 update watched unico -> logica parziale(input episodio), totale (input show) da repository

@Dao
interface SeasonDao {

    // 1. READ 000
    @Query("SELECT * FROM season_entities WHERE showId=:showId AND seasonNumber=:seasonNr")
    fun readSingleSeason(showId: Int, seasonNr: Int): Flow<SeasonEntity>

    @Query("SELECT * FROM season_entities WHERE seasonTraktId=:seasonTraktId")
    fun readSingleSeasonById(seasonTraktId: Int): Flow<SeasonEntity>


    @Query("SELECT * FROM season_entities WHERE showId=:showId")
    fun readAllSeasonsOfShow(showId: Int): Flow<List<SeasonEntity>>


    // 2. INSERT 000
    // only new items
    @Insert
    suspend fun insertSingle(entity: SeasonEntity)


    // 3.1 UPDATE dto 00
    // pesco l'elemento necessario, poi copy, modificando solo i campi necessari (da repository)
    @Update
    suspend fun updateSingleSeasonDto(entity: SeasonEntity)

    @Update
    suspend fun updateAllSeasonsDto(entities: List<SeasonEntity>)


    // 3.2 UPDATE watched (parziale, totale -> da repository)
    @Query(
        """
        UPDATE season_entities 
        SET watchedAll=:watchedAll, watchedCount=:watchedCount
        WHERE showId=:showId AND seasonNumber=:seasonNr
        """
    )
    suspend fun updateWatchedCountOfSingleSeason(
        showId: Int,
        seasonNr: Int,
        watchedAll: Boolean,
        watchedCount: Int,
    )

}
