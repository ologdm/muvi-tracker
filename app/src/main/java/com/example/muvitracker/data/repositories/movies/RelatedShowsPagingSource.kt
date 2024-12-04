package com.example.muvitracker.data.repositories.movies

//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.muvitracker.data.TraktApi
//import com.example.muvitracker.data.dto.show.toDomain
//import com.example.muvitracker.domain.model.base.Show
//import java.util.concurrent.CancellationException
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class RelatedShowsPagingSource @Inject constructor(
//    private val showId: Int,
//    private val traktApi: TraktApi
//) : PagingSource<Int, Show>() {
//
//    override suspend fun load(
//        params: LoadParams<Int>
//    ): LoadResult<Int, Show> {
//        val currentPage = params.key ?: 1
//        return try {
//            val response =
//                traktApi.getShowRelatedShowsTest(showId, currentPage, params.loadSize)
//                    .map { dto-> dto.toDomain() }
//
//            LoadResult.Page(
//                data = response,
//                prevKey = if (currentPage == 1) null else currentPage - 1,
//                nextKey = if (response.isEmpty()) null else currentPage + 1
//            )
//        } catch (ex: CancellationException) {
//            throw ex
//        } catch (ex: Exception) {
//            LoadResult.Error(ex)  // #2
//        }
//    }
//
//
//    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1)
//                ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//
//}