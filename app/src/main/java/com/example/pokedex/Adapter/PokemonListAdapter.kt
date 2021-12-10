package com.example.pokedex.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.Interface.IItemClickListener
import com.example.pokedex.Model.Pokemon
import com.example.pokedex.R
import com.example.pokedex.common.Common

class PokemonListAdapter(var context: Context, var pokemonList: List<Pokemon>) :
    RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val rootView =
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon_list_item, parent, false)
        return MyViewHolder(rootView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(pokemonList[position].img).into(holder.img_pokemon)
        holder.text_pokemon.text = pokemonList[position].name
        holder.setItemClickListener(object : IItemClickListener {
            override fun onClick(view: View, position: Int) {
              //  Toast.makeText(context, "clicked at Pokemon: " +pokemonList[position].name, Toast.LENGTH_SHORT).show()
               LocalBroadcastManager.getInstance(context)
                   .sendBroadcast(Intent(Common.KEY_ENABLE_HOME).putExtra("position",position))
            }

        })
    }


    override fun getItemCount(): Int {
        return pokemonList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var img_pokemon: ImageView
        internal var text_pokemon: TextView
        fun setItemClickListener(iItemClickListener: IItemClickListener) {
            this.itemClickListener = iItemClickListener
        }

        internal var itemClickListener: IItemClickListener? = null

        init {
            img_pokemon = itemView.findViewById(R.id.pokemon_image)
            text_pokemon = itemView.findViewById(R.id.pokemon_name)
            itemView.setOnClickListener { view -> itemClickListener!!.onClick(view, adapterPosition) }
        }
    }

}


