package com.example.pokedex.retrofit

import com.example.pokedex.Model.Pokedex
import io.reactivex.Observable
import retrofit2.http.GET

interface IPokemonList {

    @get:GET("pokedex.json")
    var listPokemon: Observable<Pokedex>
}