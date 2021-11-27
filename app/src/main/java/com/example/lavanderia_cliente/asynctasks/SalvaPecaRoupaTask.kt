package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa

class SalvaPecaRoupaTask(
    var pecaRoupaDao: PecaRoupaDao,
    var pecaRoupa: PecaRoupa,
    var listener: ListenerSalvaPeca
) :
    AsyncTask<String, String, Long>() {
    override fun doInBackground(vararg params: String?): Long {
        var id = pecaRoupaDao.salva(pecaRoupa)
        return id
    }

    override fun onPostExecute(result: Long?) {
        super.onPostExecute(result)
        listener.salvo(result)
    }

    interface ListenerSalvaPeca {
        fun salvo(id: Long?)
    }

}
