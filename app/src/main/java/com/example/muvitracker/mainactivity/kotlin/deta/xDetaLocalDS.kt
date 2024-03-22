package com.example.muvitracker.mainactivity.kotlin.deta

import com.example.muvitracker.repo.kotlin.dto.DetaDto

/**
 *  carico da locale in RAM solo all'apertura o creazione
 *  salvo ad ogni modifica CRUD
 *
 *  1° GET
 *  create, read, update, delete(no) OK
 *  getItemIndex() OK
 *  saveListInShared(), loadListFromShared() FARE
 *
 */

// 1° step solo RAM
// 2° step sharedPrefs


object xDetaLocalDS {

    // ATTRIBUTI
    private val localList = mutableListOf<DetaDto>()


    // METODI CRUD
    // create, read, update, delete


    // 1. OK
    fun createItem(dto: DetaDto) {
        

            localList.add(dto)
        println("XXX_DB_CREATE_ITEM")

        saveListInShared() // aggiorno locale
    }

    // 2. OK
    fun readItem(movieId: Int): DetaDto {
        var index = getItemIndex(movieId)

        println("XXX_DB_READ_ITEM")
        return localList.get(index)
    }


    // 3.
    fun updateItem(dto: DetaDto) {

        var index = getItemIndex(dto)

        if (index != -1) {
            localList.set(index,dto)
            // copy ssu repo, dove vaod a cambiare stato
        }
        println("XXX_DB_READ_ITEM")

        saveListInShared() // aggiorno locale
    }


    // 4. NON USARE
    private fun deleteItem(movieId: Int) {

        saveListInShared() // aggiorno locale
    }


    // METODO - IN LOCALE

    private fun saveListInShared() {
        // shared = localList
        // TODO: ad ogni modifica CRUD
    }


    // caricamento iniziale, utilizzare su
    fun loadListFromShared() : MutableList<DetaDto> {
        return localList
        // localList = shared
        // TODO: solo apertura
    }


    // METODI CHECK_ID: INDEX OK

    // scrittura: create, update OK
    fun getItemIndex(inputDto: DetaDto): Int {
        var index = -1
        for (i in localList.indices) {
            val localDto = localList.get(i)
            if (localDto.ids.trakt == inputDto.ids.trakt) {
                index = i
                break // esci dal ciclo
            }
        }
        return index
    }


    // lettura: read OK
    fun getItemIndex(inputId: Int): Int {
        var index = -1
        for (i in localList.indices) {
            val localDto = localList.get(i)
            if (localDto.ids.trakt == inputId) {
                index = i
                break
            }
        }
        return index
    }


}