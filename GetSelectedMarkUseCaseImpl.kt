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
                    MarkerData(id = it.restaurant.restaurantId, lat = it.restaurant.lat, lon = it.restaurant.lon, title = it.restaurant.restaurantName, snippet = "", foodType = it.restaurant.restaurantTypeCd, rating = "${it.restaurant.rating}", price = it.restaurant.prices)
                }.stateIn(scope = coroutineScope, started = SharingStarted.Eagerly, initialValue = MarkerData())
            }
        }
    }
}