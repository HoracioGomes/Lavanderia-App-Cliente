package com.example.lavanderia_cliente.ui.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.ClienteDao
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.database.dao.TokenDao
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel
import com.example.lavanderia_cliente.ui.viewmodel.UsuarioViewModel
import com.example.lavanderia_cliente.ui.viewmodel.factory.PecaRoupaViewModelFactory
import com.example.lavanderia_cliente.ui.viewmodel.factory.UsuarioViewModelFactory

abstract class BaseFragment : Fragment() {

    protected val pecaRoupaDao: PecaRoupaDao by lazy {
        LavanderiaDatabase.getAppDatabase(context).getPecaRoupaDao()
    }

    protected val clienteDao: ClienteDao by lazy {
        LavanderiaDatabase.getAppDatabase(context).getClienteDao()
    }

    protected val tokenDao: TokenDao by lazy {
        LavanderiaDatabase.getAppDatabase(context).getTokenDao()
    }

    protected val viewModelPecaRoupa by lazy {
        val repository = RepositoryPecaRoupa(pecaRoupaDao)
        val provider = ViewModelProviders.of(this, PecaRoupaViewModelFactory(repository))
        provider.get(PecaRoupaViewModel::class.java)
    }

    protected val viewModelUsuario by lazy {
        val repository = RepositoryUsuario(clienteDao, tokenDao)
        val provider = ViewModelProviders.of(this, UsuarioViewModelFactory(repository))
        provider.get(UsuarioViewModel::class.java)
    }

    protected val navControler by lazy {
        findNavController()
    }

}