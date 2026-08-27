package br.com.curso.rickandmorty.data.remote.dto

// Modelos API
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String,
    val gender: String,
    val origin: OriginDto
)

data class OriginDto(val name: String)

data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
)

data class EpisodeDto(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>
)

// Response Wrapper para paginação da API
data class ApiResponse<T>(
    val results: List<T>
)