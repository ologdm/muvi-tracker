package com.example.muvitracker.ui.main.detailmovie.repo

import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.ui.main.detailshow.repo.DetailShowDto
import com.example.muvitracker.utils.IoResponse
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailShowRepository @Inject constructor(
    private val traktApi: TraktApi
) {

    suspend fun getDetailData(id: Int):IoResponse<DetailShowDto> {

        return try {
            IoResponse.Success(traktApi.getShowDetail(id))
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Throwable) {
            ex.printStackTrace()
            IoResponse.Error(ex)
        }
    }


//    fun getDetailDataFlow(id: Int): Flow<DetailShowDto> = flow {
//        emit(traktApi.getShowDetail(id))
//    }


}