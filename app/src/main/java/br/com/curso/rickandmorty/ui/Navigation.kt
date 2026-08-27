package br.com.curso.rickandmorty.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import br.com.curso.rickandmorty.data.local.entity.CharacterEntity
import br.com.curso.rickandmorty.data.local.entity.EpisodeEntity
import br.com.curso.rickandmorty.data.local.entity.LocationEntity
import br.com.curso.rickandmorty.data.repository.RickRepository

@Composable
fun RickAndMortyAppNav(repository: RickRepository) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLoginScreen = currentRoute == "login"

    Scaffold(
        bottomBar = {
            if (!isLoginScreen) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(BottomNavItem.Characters.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable(BottomNavItem.Characters.route) {
                CharacterListScreen(repository = repository) { characterId ->
                    navController.navigate("character_detail/$characterId")
                }
            }
            composable(BottomNavItem.Locations.route) {
                LocationListScreen(repository = repository) { locationId ->
                    navController.navigate("location_detail/$locationId")
                }
            }
            composable(BottomNavItem.Episodes.route) {
                EpisodeListScreen(repository = repository) { episodeId ->
                    navController.navigate("episode_detail/$episodeId")
                }
            }
            composable(BottomNavItem.MyPortal.route) {
                MyPortalScreen(
                    repository = repository,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
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
fun CharacterListScreen(repository: RickRepository, onCharacterClick: (Int) -> Unit) {
    val characters by repository.allCharacters.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        repository.refreshCharacters()
    }

    if (characters.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(characters) { character ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCharacterClick(character.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = character.imageUrl,
                            contentDescription = character.name,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = character.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Status: ${character.status}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Espécie: ${character.species}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationListScreen(repository: RickRepository, onLocationClick: (Int) -> Unit) {
    val locations by repository.allLocations.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        repository.refreshLocations()
    }

    if (locations.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(locations) { location ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLocationClick(location.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = location.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Tipo: ${location.type}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Dimensão: ${location.dimension}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeListScreen(repository: RickRepository, onEpisodeClick: (Int) -> Unit) {
    val episodes by repository.allEpisodes.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        repository.refreshEpisodes()
    }

    if (episodes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(episodes) { episode ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEpisodeClick(episode.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = episode.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Código: ${episode.episode}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Lançamento: ${episode.airDate}", style = MaterialTheme.typography.bodySmall)
                    }
                }
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
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = it.imageUrl,
                contentDescription = it.name,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Status: ${it.status}")
            Text(text = "Espécie: ${it.species}")
            Text(text = "Gênero: ${if (it.gender.isNotEmpty()) it.gender else "Desconhecido"}")
            Text(text = "Origem: ${if (it.origin.isNotEmpty()) it.origin else "Desconhecida"}")
        }
    }
}

@Composable
fun LocationDetailScreen(id: Int, repository: RickRepository) {
    var location by remember { mutableStateOf<LocationEntity?>(null) }
    LaunchedEffect(id) {
        location = repository.getLocationById(id)
    }
    location?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Tipo: ${it.type}")
            Text(text = "Dimensão: ${it.dimension}")
            Text(text = "Quantidade de Residentes: ${it.residents}")
        }
    }
}

@Composable
fun EpisodeDetailScreen(id: Int, repository: RickRepository) {
    var episode by remember { mutableStateOf<EpisodeEntity?>(null) }
    LaunchedEffect(id) {
        episode = repository.getEpisodeById(id)
    }
    episode?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Código: ${it.episode}")
            Text(text = "Estreia: ${it.airDate}")
            Text(text = "Elenco (Aparecem): ${it.characters} personagens")
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Characters,
        BottomNavItem.Locations,
        BottomNavItem.Episodes,
        BottomNavItem.MyPortal
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