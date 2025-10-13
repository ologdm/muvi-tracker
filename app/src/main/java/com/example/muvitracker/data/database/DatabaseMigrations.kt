package com.example.muvitracker.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// classe che serve a descrivere le migrazioni del db
//      1 descrivo migrazione
//      2 aggiungere su hilt nel builder


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // TODO aggiungi tabella movie
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS detail_movie_entities_tmdb  (
                tmdbId INTEGER NOT NULL PRIMARY KEY,
                translation TEXT NOT NULL,
                title TEXT,
                tagline TEXT,
                overview TEXT,
                status TEXT,
                voteTmdb REAL,
                trailerLink TEXT,
                genres TEXT,
                backdropPath TEXT,
                posterPath TEXT
            )
            """.trimIndent()
        )
        // TODO aggiungi tabella show
        // TODO modifica nomi tab
    }
}


