package com.example.pokedex

import android.annotation.SuppressLint
import android.app.SearchManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.Adapter.PokemonListAdapter
import com.example.pokedex.common.Common
import com.example.pokedex.common.ItemOffsetDecoration
import com.example.pokedex.retrofit.IPokemonList
import com.example.pokedex.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class PokemonList : Fragment() {
    internal var compositeDisposable = CompositeDisposable()
    internal var iPokemonList: IPokemonList
    internal lateinit var search_bar: androidx.appcompat.widget.SearchView
    internal lateinit var recycler_View: RecyclerView
    internal lateinit var adapter: PokemonListAdapter
    internal lateinit var search_adapter: PokemonListAdapter
    internal var last_suggest: MutableList<String> = ArrayList()

    init {
        val retrofit = RetrofitClient.instance
        iPokemonList = retrofit.create(IPokemonList::class.java)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

        recycler_View = itemView.findViewById(R.id.pokemon_recyclerView) as RecyclerView
        recycler_View.layoutManager = GridLayoutManager(activity, 2)
        val itemDecoration = ItemOffsetDecoration(requireActivity(), R.dimen.spacing)
        recycler_View.addItemDecoration(itemDecoration)

        //set up search view

        /*search_bar = itemView.findViewById(R.id.search_bar)
        search_bar.setAutofillHints("Enter Pokemon name")
        search_bar.
*/

        fetchData()

        return itemView
    }

    private fun fetchData() {
        compositeDisposable.add(iPokemonList.listPokemon
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pokemonDex ->
                Common.pokemonList = pokemonDex.pokemon!!
                adapter = PokemonListAdapter(requireActivity(), Common.pokemonList)
                recycler_View.adapter = adapter
            }
        )


    }
}