package com.sarang.torang.di.map_di

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.compose.MapScreenSingleRestaurantMarker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sarang.torang.RootNavController
import com.sryang.library.compose.workflow.BestPracticeViewModel

@OptIn(ExperimentalPermissionsApi::class)
fun provideMapScreen(rootNavController: RootNavController) : @Composable (Int?)->Unit = {
    val bestPracticeViewModel : BestPracticeViewModel = hiltViewModel()
    val requestPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    MapScreenForFindingWithPermission(viewModel = bestPracticeViewModel) {
        MapScreenSingleRestaurantMarker(restaurantId = it ?: -1,
            onBack = rootNavController::popBackStack,
            requestPermission = {bestPracticeViewModel.request()},
            hasPermission = requestPermission.status.isGranted)
    }
}