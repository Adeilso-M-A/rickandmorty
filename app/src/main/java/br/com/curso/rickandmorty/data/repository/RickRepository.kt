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

    suspend fun refreshCharacters() {
        try {
            val response = api.getCharacters()
            val entities = response.results.map { dto ->
                CharacterEntity(
                    id = dto.id,
                    name = dto.name,
                    status = dto.status,
                    species = dto.species,
                    imageUrl = dto.image
                )
            }
            dao.insertCharacters(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun refreshLocations() {
        try {
            val response = api.getLocations()
            val entities = response.results.map { dto ->
                LocationEntity(
                    id = dto.id,
                    name = dto.name,
                    type = dto.type,
                    dimension = dto.dimension,
                    residents = dto.residents.size
                )
            }
            dao.insertLocations(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun refreshEpisodes() {
        try {
            val response = api.getEpisodes()
            val entities = response.results.map { dto ->
                EpisodeEntity(
                    id = dto.id,
                    name = dto.name,
                    airDate = dto.air_date,
                    episode = dto.episode,
                    characters = dto.characters.size
                )
            }
            dao.insertEpisodes(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCharacterById(id: Int): CharacterEntity? = dao.getCharacterById(id)
    suspend fun getLocationById(id: Int): LocationEntity? = dao.getLocationById(id)
    suspend fun getEpisodeById(id: Int): EpisodeEntity? = dao.getEpisodeById(id)
}