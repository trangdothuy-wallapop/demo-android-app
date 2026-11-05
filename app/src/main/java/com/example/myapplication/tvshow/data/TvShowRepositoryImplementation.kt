// Fulfil TVShowRepositoryImplementation.kt, calls API service and maps DTOs to domain
package com.example.myapplication.tvshow.data

import com.example.myapplication.tvshow.domain.TvShowError
import com.example.myapplication.tvshow.domain.TvShowModel
import com.example.myapplication.tvshow.domain.TvShowRepository
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class TvShowRepositoryImplementation(
    private val apiService: TvShowApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TvShowRepository {

    override suspend fun getShows(): Result<List<TvShowModel>> = withContext(ioDispatcher) {
        try {
            // catch here and transform to result
            val dtos = apiService.fetchShows()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            val domainError = when (e) {
                is IOException -> TvShowError.NoInternetError
                is ResponseException -> TvShowError.ServerDownError // HTTP 4xx/5xx errors
                is TvShowError -> e // Already a domain error
                else -> TvShowError.UnknownError(e)
            }
            Result.failure(domainError)
        }
    }
}
