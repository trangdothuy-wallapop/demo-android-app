// Fulfil TVShowRepositoryImplementation.kt, calls API service and maps DTOs to domain
package com.example.myapplication.tvshow.data

import com.example.myapplication.tvshow.domain.TvShowModel
import com.example.myapplication.tvshow.domain.TvShowRepository

class TvShowRepositoryImplementation(
    private val apiService: TvShowApiService = TvShowApiService()
) : TvShowRepository {

    override suspend fun getShows(): Result<List<TvShowModel>> {
        return runCatching {
            val dtos = apiService.fetchShows()
            dtos.map { it.toDomain() }
        }
    }
}
