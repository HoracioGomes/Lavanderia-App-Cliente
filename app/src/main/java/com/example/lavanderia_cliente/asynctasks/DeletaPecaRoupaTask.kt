package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa

class DeletaPecaRoupaTask(
    var dao: PecaRoupaDao,
    var pecaRoupa: PecaRoupa?,
    var listener: ListenerDeletaPecaRoupa
) : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String? {
        dao.remove(pecaRoupa)
        return pecaRoupa?.nome
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        listener.deletada(result)
    }

    interface ListenerDeletaPecaRoupa {
        fun deletada(nomePeca: String?)
    }
}
