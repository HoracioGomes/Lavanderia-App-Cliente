package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask

class BaseAsyncTask<T>(
    val enquantoExecuta: () -> T,
    val executado: (dados: T) -> Unit
) : AsyncTask<Void, Void, T>() {

    override fun doInBackground(vararg params: Void?) = enquantoExecuta()

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        executado(result)
    }

}