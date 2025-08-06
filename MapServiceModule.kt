package com.example.testapp.di.map

import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.GetMarkerListFlowUseCase
import com.example.screen_map.usecase.GetMarkerListUseCase
import com.example.screen_map.usecase.SavePositionUseCase
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.sarang.torang.api.ApiRestaurant
import com.sarang.torang.data.Filter
import com.sarang.torang.repository.MapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@InstallIn(SingletonComponent::class)
@Module
class MapServiceModule {
    @Provides
    fun provideMapService(
        restaurantApi: ApiRestaurant
    ): MapService {
        return object : MapService {
            override suspend fun restaurantMarkerList(): List<MarkerData> {
    ): GetMarkerListUseCase {
        return object : GetMarkerListUseCase {
            override suspend fun invoke(): List<MarkerData> {
                val list = restaurantApi.getFilterRestaurant(Filter())
                return list.map { MarkerData(id = it.restaurantId, lat = it.lat, lon = it.lon, title = it.restaurantName, snippet = "", foodType = it.restaurantTypeCd) }.toList()
            }
        }
    }

                val list = restaurantApi.getAllRestaurant()

                return list.stream().map {
                    MarkerData(
                        id = it.restaurantId,
                        lat = it.lat,
                        lon = it.lon,
                        title = it.restaurantName,
                        snippet = "",
                        foodType = it.restaurantType
                    )
                }.toList()
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
                return CameraPosition(
                    LatLng(mapRepository.loadLat(), mapRepository.loadLon()),
                    mapRepository.loadZoom(),
                    0f,
                    0f
                )
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