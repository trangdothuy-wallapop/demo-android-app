// Fulfil TVShowRepositoryImplementation.kt, calls API service and maps DTOs to domain
package com.example.myapplication.tvshow.data

import com.example.myapplication.tvshow.domain.TvShowModel
import com.example.myapplication.tvshow.domain.TvShowRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TvShowRepositoryImplementation(
    private val apiService: TvShowApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TvShowRepository {

    override suspend fun getShows(): Result<List<TvShowModel>> = withContext(ioDispatcher) {
        runCatching {
            val dtos = apiService.fetchShows()
            dtos.map { it.toDomain() }
        }
    }
}
