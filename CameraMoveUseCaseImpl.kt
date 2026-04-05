package com.sarang.torang.di.map_di

import com.example.screen_map.usecase.CameraMoveUseCase
import com.google.android.gms.maps.model.LatLng
import com.sarang.torang.di.repository.FindRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.StateFlow

@InstallIn(SingletonComponent::class)
@Module
class CameraMoveUseCaseImpl {

    @Provides
    fun provideCameraMoveUseCase(
        findRepository: FindRepositoryImpl
    ) : CameraMoveUseCase {
        return object : CameraMoveUseCase{
            override fun invoke(): StateFlow<Triple<Double, Double, Float>?> {
                return findRepository.cameraPosition
            }
        }
    }
}