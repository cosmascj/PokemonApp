package com.example.pokedex

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.pokedex.common.Common
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var nav_view: BottomNavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private val showDetail = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //if (intent != null) {
            if (intent!!.action!!.toString() == Common.KEY_ENABLE_HOME) {

                //Enable action bar to display the name of each selected pokemon

                supportActionBar?.setDisplayShowHomeEnabled(true)
                var detailFragment = PokemonDetail.getInstance()
                var position = intent.getIntExtra("position", -1)
                val bundle = Bundle()
                bundle.putInt("position", position)
                detailFragment.arguments = bundle


               // Set up the fragent transaction to pop up the details of each pokemon
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.pokemon_fragment, detailFragment)
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commit()

                val pokemon = Common.pokemonList[position]
                toolbar.title = pokemon.name
            }
            //}
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle("Pokemon List")
        //  setSupportActionBar(toolbar)

        // RegisterBroadcast

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showDetail, IntentFilter(Common.KEY_ENABLE_HOME))
        val pokemonFragment = PokemonList()

        setCurrentFragment(pokemonFragment)

        nav_view = findViewById(R.id.nav_view)

        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> openUploadActivity()
                R.id.navigation_dashboard -> openPokemonActivity()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                toolbar.title = "Pokemon List"
                // clear all fragment in stack with name detail
                supportFragmentManager.popBackStack(
                    "detail",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                supportActionBar!!.setDisplayShowHomeEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)

            }
        }
        return true
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.pokemon_fragment, fragment)
            commit()
        }

    private fun openPokemonActivity() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun openUploadActivity() {
        Intent(this, UploadImageMainActivity::class.java).apply {
            startActivity(this)
        }
    }
}