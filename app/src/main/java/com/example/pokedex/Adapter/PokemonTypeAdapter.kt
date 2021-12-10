package com.example.pokedex.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.Interface.IItemClickListener
import com.example.pokedex.R
import com.example.pokedex.common.Common
import com.robertlevonyan.views.chip.Chip

class PokemonTypeAdapter(internal var context: Context, internal var typeList: List<String>):
    RecyclerView.Adapter<PokemonTypeAdapter.MyViewHolder>() {





        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            internal var chip: Chip
            internal var iItemClickListener: IItemClickListener? = null

            fun setItemClickListener(iItemClickListener: IItemClickListener){

                this.iItemClickListener = iItemClickListener
            }

            init {
                chip = itemView.findViewById(R.id.chip) as Chip
                chip.setOnChipClickListener { view -> iItemClickListener!!.onClick(view, adapterPosition) }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

       val itemView = LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.chip.chipText = typeList[position]
        holder.chip.changeBackgroundColor(Common.getColorByType(typeList[position]))


    }

    override fun getItemCount(): Int {
return typeList.size
    }


}