package com.example.lavanderia_cliente.di

import androidx.navigation.NavController
import androidx.room.Room
import com.example.lavanderia_cliente.database.LavanderiaDatabase
import com.example.lavanderia_cliente.database.dao.ClienteDao
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.database.dao.TokenDao
import com.example.lavanderia_cliente.repository.RepositoryPecaRoupa
import com.example.lavanderia_cliente.repository.RepositoryUsuario
import com.example.lavanderia_cliente.ui.recyclerview.adapter.ListaRoupasAdapter
import com.example.lavanderia_cliente.ui.viewmodel.PecaRoupaViewModel
import com.example.lavanderia_cliente.ui.viewmodel.UsuarioViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DB = "lavanderia_db"

val appModuleDb = module {
    single<LavanderiaDatabase> {
        Room.databaseBuilder(get(), LavanderiaDatabase::class.java, DB)
            //                .addMigrations(
            //                    *LavanderiaMigrations().MIGRATIONS
            //                )
            .fallbackToDestructiveMigration()
            .build()

    }
}

val appModuleDao = module {
    single<PecaRoupaDao> {
        get<LavanderiaDatabase>().getPecaRoupaDao()
    }
    single<ClienteDao> {
        get<LavanderiaDatabase>().getClienteDao()
    }
    single<TokenDao> {
        get<LavanderiaDatabase>().getTokenDao()
    }
}

val appModuleRepository = module {

    single<RepositoryPecaRoupa> {
        RepositoryPecaRoupa(dao = get<PecaRoupaDao>())
    }
    single<RepositoryUsuario> {
        RepositoryUsuario(clienteDao = get<ClienteDao>(), tokenDao = get<TokenDao>())
    }
}

val appModuleViewModel = module {
    viewModel {
        PecaRoupaViewModel(repository = get<RepositoryPecaRoupa>())
    }

    viewModel {
        UsuarioViewModel(repository = get<RepositoryUsuario>())
    }
}

//val uiModule = module {
//    factory<ListaRoupasAdapter> {
//        (navController: NavController) ->
//        ListaRoupasAdapter(
//            context = get(),
//            viewModelRoupa = get<PecaRoupaViewModel>(),
//            navController = navController
//        )
//    }
//}

val appModule = listOf(
    appModuleDb,
    appModuleDao,
    appModuleRepository,
    appModuleViewModel,
//    uiModule
)
