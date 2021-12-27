package com.example.lavanderia_cliente.ui.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lavanderia_cliente.R
import com.example.lavanderia_cliente.model.Cliente
import com.example.lavanderia_cliente.model.Token


class MainActivity : AppCompatActivity() {

    companion object {
        var cliente: Cliente? = null
        var token: Token? = null
        lateinit var mToogle: ActionBarDrawerToggle
    }

    private val navController: NavController by lazy {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.container_primario_nav_host_fragment) as NavHostFragment
        navHost.navController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = destination.label

            when (destination.id) {
                R.id.login -> supportActionBar?.hide()
                R.id.listaPecasRoupas -> supportActionBar?.show()
                R.id.formularioDelivery -> supportActionBar?.show()
            }
        }

    }


}