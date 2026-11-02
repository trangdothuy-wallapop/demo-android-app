package com.example.myapplication.di

import com.example.myapplication.greeting.GreetingViewModel
import com.example.myapplication.counter.CounterViewModel
import com.example.myapplication.tvshow.ui.TvShowViewModel
import com.example.myapplication.tvshow.data.TvShowApiService
import com.example.myapplication.tvshow.data.TvShowRepositoryImplementation
import com.example.myapplication.tvshow.domain.TvShowRepository
import com.example.myapplication.tvshow.domain.TvShowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val AppModule = module {
    // Coroutine Dispatcher IO
    single<CoroutineDispatcher> { Dispatchers.IO }

    // Network: Ktor HttpClient & ApiService
    single { TvShowApiService.createKtorClient() }
    single { TvShowApiService(get()) }

    // Data layer: Repository
    single<TvShowRepository> {
        TvShowRepositoryImplementation(
            apiService = get(),
            ioDispatcher = get()
        )
    }

    // Domain layer: UseCase
    factory { TvShowUseCase(get()) }
//    another way to write: factoryOf(::TvShowUseCase)

    // UI layer: ViewModel
    viewModelOf(::GreetingViewModel)
    viewModelOf(::CounterViewModel)
    viewModel {
        val useCase: TvShowUseCase = get()
        TvShowViewModel(
            useCase = useCase::invoke
        )
    }
}