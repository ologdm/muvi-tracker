package com.example.muvitracker.data

import androidx.room.Database
import androidx.room.Insert
import com.dropbox.android.external.store4.Store
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.dao.SeasonDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XEpisodeRepository @Inject constructor(
   private val traktApi: TraktApi,
   private val database: MyDatabase
) {





}