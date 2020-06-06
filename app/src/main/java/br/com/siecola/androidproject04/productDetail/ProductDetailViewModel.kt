package br.com.siecola.androidproject04.productDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.siecola.androidproject04.persistence.Product
import br.com.siecola.androidproject04.persistence.ProductRepository


private const val TAG = "ProductDetailViewModel"

class ProductDetailViewModel(private val code: String?): ViewModel() {

    lateinit var product : MutableLiveData<Product>

    init {
        if (code != null) {
            getProduct(code)
        } else {
            product = MutableLiveData<Product>()
            product.value = Product()
        }
    }

    private fun getProduct(productCode: String) {
        product = ProductRepository.getProductByCode(productCode)
    }

    override fun onCleared() {
        if (product.value != null) {
            ProductRepository.saveProduct(product.value!!)
        }
        super.onCleared()
    }

    fun deleteProduct() {
        if (product.value?.id != null) {
            ProductRepository.deleteProduct(product.value!!.id!!)
            product.value = null
        }
    }

}