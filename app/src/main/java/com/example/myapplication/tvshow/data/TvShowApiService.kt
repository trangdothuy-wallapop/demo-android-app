// API Service with Ktor
package com.example.myapplication.tvshow.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TvShowApiService(
    private val client: HttpClient = createKtorClient()
) {
    // Fetches TV shows from https://api.tvmaze.com/shows?page=1
    suspend fun fetchShows(page: Int = 1): List<TvShowDto> {
        return client.get("https://api.tvmaze.com/shows?page=$page").body()
    }

    companion object {
        fun createKtorClient(): HttpClient {
            return HttpClient(Android) {
                install(ContentNegotiation) { // To handle JSON serialization/deserialization
                    json(Json {
                        prettyPrint = true // Formats JSON nicely if printed out
                        isLenient = true // Prevents crashes if the API returns slightly imperfectly formatted JSON
                        ignoreUnknownKeys = true // Ignore TVMaze JSON fields we don't care about
                    })
                }
            }
        }
    }
}
