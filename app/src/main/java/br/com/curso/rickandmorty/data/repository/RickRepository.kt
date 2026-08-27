package br.com.curso.rickandmorty.data.repository

import br.com.curso.rickandmorty.data.local.dao.RickDao
import br.com.curso.rickandmorty.data.local.entity.CharacterEntity
import br.com.curso.rickandmorty.data.local.entity.EpisodeEntity
import br.com.curso.rickandmorty.data.local.entity.LocationEntity
import br.com.curso.rickandmorty.data.local.entity.PortalLocationEntity
import br.com.curso.rickandmorty.data.remote.RickApi
import kotlinx.coroutines.flow.Flow

class RickRepository(
    private val api: RickApi,
    private val dao: RickDao
) {
    val allCharacters: Flow<List<CharacterEntity>> = dao.getAllCharacters()
    val allLocations: Flow<List<LocationEntity>> = dao.getAllLocations()
    val allEpisodes: Flow<List<EpisodeEntity>> = dao.getAllEpisodes()
    val portalLocation: Flow<PortalLocationEntity?> = dao.getPortalLocation()

    suspend fun getCharacterById(id: Int): CharacterEntity? {
        return dao.getCharacterById(id)
    }

    suspend fun getLocationById(id: Int): LocationEntity? {
        return dao.getLocationById(id)
    }

    suspend fun getEpisodeById(id: Int): EpisodeEntity? {
        return dao.getEpisodeById(id)
    }

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        dao.insertCharacters(characters)
    }

    suspend fun insertLocations(locations: List<LocationEntity>) {
        dao.insertLocations(locations)
    }

    suspend fun insertEpisodes(episodes: List<EpisodeEntity>) {
        dao.insertEpisodes(episodes)
    }

    suspend fun savePortalLocation(location: PortalLocationEntity) {
        dao.insertPortalLocation(location)
    }
}