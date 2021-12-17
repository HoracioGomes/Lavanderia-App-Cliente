package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa

class BuscaPecasRoupaTask(
    var pecaRoupaDao: PecaRoupaDao,
    var listener: ListenerBuscaPecasRoupa
) : AsyncTask<Void, Void, Long >() {
    override fun doInBackground(vararg params: Void?): Long {
        return 0
    }

    override fun onPostExecute(result: Long) {
        super.onPostExecute(result)
        listener.retorno(result)
    }

    public interface ListenerBuscaPecasRoupa {
        fun retorno(pecasRoupasRetornadas: Long)

    }
}
