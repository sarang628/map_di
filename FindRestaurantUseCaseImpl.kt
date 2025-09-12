package com.sarang.torang.di.map_di

import android.util.Log
import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.FindRestaurantUseCase
import com.sarang.torang.api.ApiRestaurant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class FindRestaurantUseCaseImpl {
    @Provides
    fun provideFindRestaurantUseCase(
        apiRestaurant: ApiRestaurant
    ) : FindRestaurantUseCase{
        return object : FindRestaurantUseCase{
            override suspend fun invoke(restaurantId: Int): MarkerData {
                val result = apiRestaurant.getRestaurantById(restaurantId = restaurantId)
                Log.d("__provideFindRestaurantUseCase", "restaurantId : $restaurantId, result : $result")
                return MarkerData(
                    id = result.restaurantId ?: 0,
                    lat = result.lat ?: 0.0,
                    lon = result.lon ?: 0.0,
                    title = result.restaurantName ?: "",
                    rating = result.rating.toString(),
                )
            }
        }
    }
}