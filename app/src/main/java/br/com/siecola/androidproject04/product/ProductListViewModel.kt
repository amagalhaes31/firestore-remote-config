package br.com.siecola.androidproject04.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.siecola.androidproject04.persistence.Product
import br.com.siecola.androidproject04.persistence.ProductRepository

private const val TAG = "ProductListViewModel"

class ProductListViewModel : ViewModel() {

    private var _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
        get() = _products

    init {
        Log.i(TAG, "init ViewModel")
        getProducts()
    }

    private fun getProducts() {
        _products = ProductRepository.getProducts()     // Acesso o repositório de dados
    }

    override fun onCleared() {
        super.onCleared()
    }
}