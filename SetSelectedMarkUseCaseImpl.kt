package com.sarang.torang.di.map_di

import com.example.screen_map.usecase.SetSelectedMarkUseCase
import com.sarang.torang.di.repository.repository.impl.FindRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class SetSelectedMarkUseCaseImpl {
    @Provides
    fun provideMapService(
        findRepository: FindRepositoryImpl
    ): SetSelectedMarkUseCase {
        return object : SetSelectedMarkUseCase {
            override suspend fun invoke(restaurantId : Int) {
                findRepository.selectRestaurant(restaurantId)
        }}
    }
}