package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask

abstract class BaseAsyncTask(var listener: ListenerBaseAsync) : AsyncTask<Void, Void, Void>() {

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        listener.finalizado()
    }

    interface ListenerBaseAsync {
        fun finalizado()
    }
}