package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask

class BaseAsyncTask<T>(
    var listenerExecuta: ListenerBaseAsyncExecuta<T>,
    var listenerFinaliza: ListenerBaseAsyncFinaliza<T>
) : AsyncTask<Void, Void, T>() {

    override fun doInBackground(vararg params: Void?): T {

        return listenerExecuta.executando()

    }

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        listenerFinaliza.finalizado(result)
    }

    interface ListenerBaseAsyncExecuta<T> {
        fun executando(): T
    }

    interface ListenerBaseAsyncFinaliza<T> {
        fun finalizado(result: T)
    }

}