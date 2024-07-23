package com.example.muvitracker.data.detail

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.example.muvitracker.data.dto.basedto.Ids

// OK
@Entity (tableName = "DetailEntities")
data class DetailEntityR (
    @PrimaryKey val traktId : Int,
    val title: String,
    val year: Int,
    val ids: Ids,
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String,
    val rating: Float,
    val genres: List<String>,
)



@Dao
interface DetailDao {

    // get allData OK
    @Query("SELECT * FROM DetailEntities") // tutti i campi
    fun getAll() : List<DetailEntityR>

    // get single element
    @Query("SELECT * FROM DetailEntities WHERE traktId = :inputId")
    fun getEntity (inputId:Int) : DetailEntityR

    @Insert
    fun insertEntity (entity: DetailEntityR)

//    @Insert
//    fun insertEntities ( vararg entities: DetailEntityR)  - inserire piu elementi

    @Delete
    fun deteleEntity (entity: DetailEntityR)

    @Update
    fun update (entity: DetailEntityR)



}








