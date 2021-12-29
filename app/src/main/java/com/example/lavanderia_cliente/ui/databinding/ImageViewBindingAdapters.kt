package com.example.lavanderia_cliente.ui.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.lavanderia_cliente.extensions.carregaImagem

@BindingAdapter("carregaImagem")
fun ImageView.carregaImagemPorUrl(url: String?) {
    url?.let { carregaImagem(it) }
}