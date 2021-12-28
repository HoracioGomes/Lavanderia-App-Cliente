package com.example.lavanderia_cliente.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token
import com.example.lavanderia_cliente.ui.viewmodel.ComponetesVisuais
import com.example.lavanderia_cliente.ui.viewmodel.EstadoAppViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        var cliente: Cliente? = null
        var token: Token? = null
        val viewModelEstado = EstadoAppViewModel()
        lateinit var mToogle: ActionBarDrawerToggle
    }

    private val navController: NavController by lazy {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.container_primario_nav_host_fragment) as NavHostFragment
        navHost.navController
    }

    private val bottomNavigation: BottomNavigationView by lazy {
        findViewById(R.id.activity_main_bottom_navigation)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = destination.label

            viewModelEstado.componentesVisuais.observe(this, Observer {
                it?.let { temComponentes ->
                    vizualizacaoAppBar(temComponentes)

                    vizualizacaoBottomNavigation(temComponentes)
                }
            })

        }

        gerenciadorBottomNavigation()

    }

    private fun vizualizacaoAppBar(temComponentes: ComponetesVisuais) {
        if (temComponentes.appBar) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    private fun vizualizacaoBottomNavigation(temComponentes: ComponetesVisuais) {
        if (temComponentes.bottomNavigation) {
            bottomNavigation.visibility = View.VISIBLE
        } else {
            bottomNavigation.visibility = View.GONE

        }
    }

    private fun gerenciadorBottomNavigation() {
        bottomNavigation.setupWithNavController(navController)
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            if (menuItem.title == getString(R.string.menu_item_lista)) {
                navController.popBackStack(R.id.listaPecasRoupas, true)
                navController.navigate(R.id.listaPecasRoupas)
            } else if (menuItem.title == getString(R.string.menu_item_delivery)) {
                navController.navigate(R.id.formularioDelivery)
            }
            true
        }
    }


}