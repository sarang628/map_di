package com.sarang.torang.di.map_di

import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.GetSelectedMarkUseCase
import com.sarang.torang.di.repository.repository.impl.FindRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@InstallIn(SingletonComponent::class)
@Module
class GetSelectedMarkUseCaseImpl {
    @Provides
    fun provideMapService(
        findRepository: FindRepositoryImpl
    ): GetSelectedMarkUseCase {
        return object : GetSelectedMarkUseCase {
            override fun invoke(coroutineScope: CoroutineScope): StateFlow<MarkerData> {
                return findRepository.selectedRestaurant.map {
                    MarkerData(id = it.restaurantId, lat = it.lat, lon = it.lon, title = it.restaurantName, snippet = "", foodType = it.restaurantType)
                }.stateIn(scope = coroutineScope, started = SharingStarted.Eagerly, initialValue = MarkerData())
            }
        }
    }
}