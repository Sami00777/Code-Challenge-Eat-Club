package com.eatclub.challenge.di

import android.content.Context
import com.eatclub.challenge.data.network.ApiService
import com.eatclub.challenge.data.repository.RestaurantRepositoryImpl
import com.eatclub.challenge.data.network.NetworkMonitor
import com.eatclub.challenge.domain.repository.FetchRestaurantsUseCase
import com.eatclub.challenge.domain.repository.FilterRestaurantsUseCase
import com.eatclub.challenge.domain.repository.RestaurantRepository
import com.eatclub.challenge.domain.usecase.FetchRestaurantsUseCaseImpl
import com.eatclub.challenge.domain.usecase.FilterRestaurantsUseCaseImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

internal const val BASE_URL = "https://eccdn.com.au/"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRestaurantRepository(apiService: ApiService): RestaurantRepository {
        return RestaurantRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideFilteringUseCase(): FilterRestaurantsUseCase {
        return FilterRestaurantsUseCaseImpl()
    }

    @Provides
    @Singleton
    fun provideFetchRestaurantsUseCase(repository: RestaurantRepository): FetchRestaurantsUseCase {
        return FetchRestaurantsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context,
    ): NetworkMonitor = NetworkMonitor(context)
}
