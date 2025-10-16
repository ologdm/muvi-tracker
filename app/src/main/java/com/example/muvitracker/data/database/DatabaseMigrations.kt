package com.example.muvitracker.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// classe che serve a descrivere le migrazioni del db
//      1 descrivo migrazione
//      2 aggiungere su hilt nel builder


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // TODO:
        // 1️⃣ Creiamo la nuova tabella con la nuova struttura OK
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
                country TEXT,
                runtime INTEGER,
                originalLanguage TEXT,
                originalTitle TEXT,
                genres TEXT NOT NULL,
                youtubeTrailer TEXT,
                homepage TEXT,
                backdropPath TEXT,
                posterPath TEXT,
                traktRating REAL,
                tmdbRating REAL,
                currentTranslation TEXT NOT NULL
            )
        """.trimIndent()
        )

        // 2️⃣ Copiamo i dati dalla vecchia tabella a quella nuova
        database.execSQL(
            """
            INSERT INTO detail_movie (
                traktId, year, ids, title, tagline, overview, status,
                releaseDate, country, runtime, originalLanguage, originalTitle,
                genres, youtubeTrailer, homepage, backdropPath, posterPath,
                traktRating, tmdbRating, currentTranslation
            )
            
            SELECT 
                traktId, year, ids, title, tagline, overview, status,
                released, country, runtime, language, NULL,
                genres, trailer, homepage, NULL, NULL,
                rating, NULL, 'en-EN'
            FROM detail_movie_entities
            
        """.trimIndent())

        // 3️⃣ Eliminiamo la vecchia tabella
        database.execSQL("DROP TABLE detail_movie_entities")
    }
}


//val MIGRATION_2_3 = object : Migration(2, 3) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            """
//            CREATE TABLE IF NOT EXISTS detail_show_entities_tmdb  (
//                tmdbId INTEGER NOT NULL PRIMARY KEY,
//                translation TEXT NOT NULL,
//                title TEXT,
//                tagline TEXT,
//                overview TEXT,
//                voteTmdb REAL,
//                trailerLink TEXT,
//                genres TEXT NOT NULL,
//                backdropPath TEXT,
//                posterPath TEXT
//            )
//            """.trimIndent()
//        )
//    }
//}


//val MIGRATION_3_4 = object : Migration(3, 4) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            """
//            CREATE TABLE IF NOT EXISTS season_entities_tmdb  (
//                seasonTmdbId INTEGER NOT NULL PRIMARY KEY,
//                translation TEXT NOT NULL,
//                seasonNumber INTEGER,
//                name TEXT,
//                overview TEXT,
//                posterPath TEXT,
//                voteTmdb REAL
//            )
//            """.trimIndent()
//        )
//    }
//}


