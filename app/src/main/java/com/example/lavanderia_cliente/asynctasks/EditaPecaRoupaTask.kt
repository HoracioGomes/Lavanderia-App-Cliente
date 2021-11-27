package com.example.lavanderia_cliente.asynctasks

import android.os.AsyncTask
import com.example.lavanderia_cliente.database.dao.PecaRoupaDao
import com.example.lavanderia_cliente.model.PecaRoupa

class EditaPecaRoupaTask(
    var pecaRoupaDao: PecaRoupaDao,
    var pecasRoupa: MutableList<PecaRoupa?>,
    var listener: ListenerEditaPecaRoupa
) :
    AsyncTask<String, String, String>() {

    override fun doInBackground(vararg params: String?): String? {
        if (pecasRoupa.size > 1) {
            pecasRoupa.forEach {
                pecaRoupaDao.edita(it)

            }
            return ""
        } else {
            pecaRoupaDao.edita(pecasRoupa[0])
            return pecasRoupa[0]?.nome
        }

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        listener.editado(result)
    }

    interface ListenerEditaPecaRoupa {
        fun editado(nomePeca: String?)
    }

}
