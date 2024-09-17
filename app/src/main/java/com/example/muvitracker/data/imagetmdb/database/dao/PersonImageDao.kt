package com.example.muvitracker.data.imagetmdb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muvitracker.data.imagetmdb.database.entities.PersonImageEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PersonImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleElement(element: PersonImageEntity)


    @Query("SELECT * FROM person_image_entities WHERE id=:personId ")
    fun readSingle(personId: Int): Flow<PersonImageEntity>

}