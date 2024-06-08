package com.example.muvitracker.xappunti


// DETAIL REPOSITORY

//private fun getNetworkAndAddToLocal(
//    movieId: Int,
//    onResponse: (IoResponse<DetailMovie>) -> Unit,
//) {
//
//    DetailNetworkDS.callDetailServer(
//        movieId,
//        onResponse = { retrofitResponse ->
//
//            val mappedResponse = retrofitResponse.ioMapper { dto ->
//                val detailEntity = dto.toEntity()
//
//                val prefsItem = prefsRepo.getLivedata().value?.find {
//                    movieId == it.movieId
//                }
//
//                if (prefsItem != null) {
//                    val detailMovie = detailEntity.toDomain(prefsItem)
//                    detailMovie
//                } else {
//                    detailEntity.toDomain(null)
//                }
//            }
//            onResponse(mappedResponse) // trasformo in DetailMovie
//
//            // success - save net data on db
//            if (retrofitResponse is IoResponse.Success) {
//                val entity = retrofitResponse.dataValue.toEntity()
//                detailLocalDS.addOrUpdateItem(entity) // trasformo in DetailEntity
//            }
//        }
//    )
//}


// PREFS LOCAL
//    fun toggleFavoriteOnDB(id: Int) {
//        val cache = readCache().toMutableList()
//        val index = cache.indexOfFirst { it.movieId == id }
//        if (index != -1) {
//            val current = cache[index]
//            cache[index] = current.copy(liked = !current.liked)
//            // (dima)
//            if (current.liked==false && current.watched == false){
//                cache.removeAt(index)
//            }
//        } else {
//            // corretto
//            cache.add(PrefsEntity(liked = true, watched = false, movieId = id))
//        }
//        saveListInShared(cache)
//    }
//
//
//    fun updateWatchedOnDB(id: Int, watched: Boolean) {
//        val cache = readCache().toMutableList()
//        val index = cache.indexOfFirst { it.movieId == id }
//        if (index != -1) {
//            val current = cache[index]
//            cache.set(index, current.copy(watched = watched))
//            // (dima)
//            if (current.liked==false && current.watched == false){
//                cache.removeAt(index)
//            }
//        } else {
//            cache.add(PrefsEntity(liked = false, watched = watched, movieId = id))
//        }
//        saveListInShared(cache)
//    }