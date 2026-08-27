package br.com.curso.rickandmorty.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.curso.rickandmorty.data.local.entity.CharacterEntity
import br.com.curso.rickandmorty.data.local.entity.EpisodeEntity
import br.com.curso.rickandmorty.data.local.entity.LocationEntity
import br.com.curso.rickandmorty.data.repository.RickRepository

@Composable
fun RickAndMortyAppNav(repository: RickRepository) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Characters.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Characters.route) {
                Text("Tela de Personagens")
            }
            composable(BottomNavItem.Locations.route) {
                Text("Tela de Locais")
            }
            composable(BottomNavItem.Episodes.route) {
                Text("Tela de Episódios")
            }
            composable("character_detail/{characterId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("characterId")?.toIntOrNull() ?: 0
                CharacterDetailScreen(id = id, repository = repository)
            }
            composable("location_detail/{locationId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("locationId")?.toIntOrNull() ?: 0
                LocationDetailScreen(id = id, repository = repository)
            }
            composable("episode_detail/{episodeId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("episodeId")?.toIntOrNull() ?: 0
                EpisodeDetailScreen(id = id, repository = repository)
            }
        }
    }
}

@Composable
fun CharacterDetailScreen(id: Int, repository: RickRepository) {
    var character by remember { mutableStateOf<CharacterEntity?>(null) }
    LaunchedEffect(id) {
        character = repository.getCharacterById(id)
    }
    character?.let {
        Text(text = "Detalhes do Personagem: ${it.name}")
    }
}

@Composable
fun LocationDetailScreen(id: Int, repository: RickRepository) {
    var location by remember { mutableStateOf<LocationEntity?>(null) }
    LaunchedEffect(id) {
        location = repository.getLocationById(id)
    }
    location?.let {
        Text(text = "Detalhes da Localização: ${it.name}")
    }
}

@Composable
fun EpisodeDetailScreen(id: Int, repository: RickRepository) {
    var episode by remember { mutableStateOf<EpisodeEntity?>(null) }
    LaunchedEffect(id) {
        episode = repository.getEpisodeById(id)
    }
    episode?.let {
        Text(text = "Detalhes do Episódio: ${it.name}")
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Characters,
        BottomNavItem.Locations,
        BottomNavItem.Episodes
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}