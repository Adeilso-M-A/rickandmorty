package br.com.curso.rickandmorty.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Characters : BottomNavItem("characters", "Personagens", Icons.Default.Face)
    object Locations : BottomNavItem("locations", "Locais", Icons.Default.LocationOn)
    object Episodes : BottomNavItem("episodes", "Episódios", Icons.Default.PlayArrow)
    object MyPortal : BottomNavItem("myPortal", "Meu Portal", Icons.Default.Place)
}