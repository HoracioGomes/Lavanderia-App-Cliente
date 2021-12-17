package com.example.lavanderia_cliente.repository

class Resource<T>(val dados: T?, val erro: String? = null)

fun <T> criaResourceDeFalha(
    resourceAntigo: Resource<T?>?,
    mensagem: String?
): Resource<T?> {
    return if (resourceAntigo?.dados != null) {
        Resource(erro = mensagem, dados = resourceAntigo.dados)
    } else {
        Resource(erro = mensagem, dados = null)
    }
}
