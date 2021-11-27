package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa

class BuscaPecasRoupaTask(
    var pecaRoupaDao: PecaRoupaDao,
    var listener: ListenerBuscaPecasRoupa
) : AsyncTask<Void, Void, MutableList<PecaRoupa>>() {
    override fun doInBackground(vararg params: Void?): MutableList<PecaRoupa> {
        return pecaRoupaDao.todas()
    }

    override fun onPostExecute(result: MutableList<PecaRoupa>?) {
        super.onPostExecute(result)
        listener.retorno(result)
    }

    public interface ListenerBuscaPecasRoupa {
        fun retorno(pecasRoupasRetornadas: MutableList<PecaRoupa>?)

    }
}
