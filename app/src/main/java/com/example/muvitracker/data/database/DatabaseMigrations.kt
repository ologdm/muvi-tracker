package com.example.muvitracker.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// classe che serve a descrivere le migrazioni del db
//      1 descrivo migrazione
//      2 aggiungere su hilt nel builder


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // 1. Creiamo nuova tabella
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS detail_movie (
                traktId INTEGER PRIMARY KEY NOT NULL,
                year INTEGER,
                ids TEXT NOT NULL,
                title TEXT,
                tagline TEXT,
                overview TEXT,
                status TEXT,
                releaseDate TEXT,
                country TEXT NOT NULL,
                runtime INTEGER,
                originalLanguage TEXT,
                originalTitle TEXT,
                genres TEXT NOT NULL,
                youtubeTrailer TEXT,
                homepage TEXT,
                backdropPath TEXT,
                posterPath TEXT,
                traktRating TEXT,
                tmdbRating TEXT,
                currentTranslation TEXT NOT NULL
            )
        """.trimIndent()
        )

        // 2. Copia dati compatibili dalla vecchia tabella
        database.execSQL(
            """
            INSERT INTO detail_movie (
                traktId, 
                year, 
                ids, 
                title, 
                tagline, 
                overview, 
                status,
                releaseDate, 
                country, 
                runtime, 
                originalLanguage, 
                originalTitle, 
                genres, 
                youtubeTrailer, 
                homepage, 
                backdropPath, 
                posterPath,
                traktRating, 
                tmdbRating, 
                currentTranslation
            )
            SELECT 
                traktId, 
                year,
                ids, 
                title, 
                tagline, 
                overview, 
                status,
                released AS releaseDate, 
                '[]' AS country, 
                runtime, 
                language AS originalLanguage, 
                NULL AS originalTitle, 
                genres, 
                trailer AS youtubeTrailer, 
                homepage, 
                NULL AS backdropPath , 
                NULL AS posterPath,
                rating AS traktRating, 
                NULL AS tmdbRating, 
                'en-EN' AS currentTranslation
            FROM detail_movie_entities
            
        """.trimIndent()
        )


        // 3. Eliminiamo la vecchia tabella
        database.execSQL("DROP TABLE detail_movie_entities")
    }
}
