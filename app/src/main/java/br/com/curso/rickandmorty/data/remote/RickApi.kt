package br.com.curso.rickandmorty.data.remote

import br.com.curso.rickandmorty.data.remote.dto.ApiResponse
import br.com.curso.rickandmorty.data.remote.dto.CharacterDto
import br.com.curso.rickandmorty.data.remote.dto.EpisodeDto
import br.com.curso.rickandmorty.data.remote.dto.LocationDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RickApi {

    @GET("character")
    suspend fun getCharacters(): ApiResponse<CharacterDto>

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDto

    @GET("location")
    suspend fun getLocations(): ApiResponse<LocationDto>

    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: Int): LocationDto

    @GET("episode")
    suspend fun getEpisodes(): ApiResponse<EpisodeDto>

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): EpisodeDto

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        fun create(): RickApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RickApi::class.java)
        }
    }
}