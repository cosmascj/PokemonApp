package com.example.pokedex.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var ourInstance: Retrofit? = null
    val instance:Retrofit
    get() {
        if (ourInstance == null)
            ourInstance = Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/Biuni/PokemonGo-Pokedex/master/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return ourInstance!!


    }
}