package com.sarang.torang.di.map_di

import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.GetMarkerListFlowUseCase
import com.example.screen_map.usecase.GetMarkerListUseCase
import com.example.screen_map.usecase.SavePositionUseCase
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.sarang.torang.api.ApiRestaurant
import com.sarang.torang.data.Filter
import com.sarang.torang.data.remote.response.FilterApiModel
import com.sarang.torang.repository.FindRepository
import com.sarang.torang.repository.MapRepository
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
class MapServiceModule {
    @Provides
    fun provideMapService(
        restaurantApi: ApiRestaurant
    ): GetMarkerListUseCase {
        return object : GetMarkerListUseCase {
            override suspend fun invoke(): List<MarkerData> {
                val list = restaurantApi.getFilterRestaurant(FilterApiModel())
                return list.map { MarkerData(id = it.restaurantId ?: -1, lat = it.lat ?: 0.0, lon = it.lon ?: 0.0, title = it.restaurantName ?: "", snippet = "", foodType = it.restaurantTypeCd ?: "") }.toList()
            }
        }
    }

    @Provides
    fun provideGetMarkerListFlowUseCase(
        findRepository: FindRepository
    ): GetMarkerListFlowUseCase {
        return object : GetMarkerListFlowUseCase {
            override fun invoke(coroutineScope : CoroutineScope): StateFlow<List<MarkerData>> {
                return findRepository.restaurants
                    .map { list ->
                        list.map { item -> MarkerData(id = item.restaurant.restaurantId, lat = item.restaurant.lat, lon = item.restaurant.lon, title = item.restaurant.restaurantName, snippet = "", foodType = item.restaurant.restaurantTypeCd, rating = item.restaurant.rating.toString(), price = item.restaurant.prices)}
                    }.stateIn(scope = coroutineScope, started = SharingStarted.Eagerly, initialValue = emptyList())
            }
        }
    }

    @Provides
    fun provideSavePositionUseCase(
        mapRepository: MapRepository
    ): SavePositionUseCase {
        return object : SavePositionUseCase {
            override fun save(position: CameraPosition) {
                mapRepository.saveLat(position.target.latitude)
                mapRepository.savelon(position.target.longitude)
                mapRepository.saveZoom(position.zoom)
            }

            override fun load(): CameraPosition {
                return CameraPosition(LatLng(mapRepository.loadLat(), mapRepository.loadLon()), mapRepository.loadZoom(), 0f, 0f)
            }

            override fun saveBound(visibleRegion: VisibleRegion) {
                mapRepository.setNElat(visibleRegion.latLngBounds.northeast.latitude)
                mapRepository.setNElon(visibleRegion.latLngBounds.northeast.longitude)
                mapRepository.setSWlat(visibleRegion.latLngBounds.southwest.latitude)
                mapRepository.setSWlon(visibleRegion.latLngBounds.southwest.longitude)
            }
        }
    }
}