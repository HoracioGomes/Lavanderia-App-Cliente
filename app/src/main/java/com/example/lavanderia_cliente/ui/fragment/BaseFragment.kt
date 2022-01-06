package com.example.lavanderia_cliente.ui.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel
import com.example.lavanderia_cliente.ui.viewmodel.UsuarioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment : Fragment() {
    protected val viewModelPecasRoupa: PecaRoupaViewModel by viewModel()
    protected val viewModelUsuario: UsuarioViewModel by viewModel()

    protected val navControler by lazy {
        findNavController()
    }

}