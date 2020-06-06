package br.com.siecola.androidproject04.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "ProductRepository"
private const val COLLECTION = "products"
private const val FIELD_USER_ID = "userId"
private const val FIELD_NAME = "name"
private const val FIELD_DESCRIPTION = "description"
private const val FIELD_CODE = "code"
private const val FIELD_PRICE = "price"

object ProductRepository {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // Cria um produto no firestore
    fun saveProduct(product: Product): String {
        val document = if (product.id != null) {
            firebaseFirestore.collection(COLLECTION).document(product.id!!)
        } else {
            product.userId = firebaseAuth.getUid()!!                // Ou usar o "product.userId = firebaseAuth.uid!!"
            firebaseFirestore.collection(COLLECTION).document()
        }
        document.set(product)       // Salva o documento
        return document.id          // Retorna a identificação unica do documento criado
    }

    // Apaga um produto na coleção "products"
    fun deleteProduct(productId: String) {
        val document = firebaseFirestore.collection(COLLECTION).document(productId)
        document.delete()
    }

    // Lista todos os produtos
    fun getProducts(): MutableLiveData<List<Product>> {
        val liveProducts = MutableLiveData<List<Product>>()

        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_USER_ID, firebaseAuth.uid)                                          // Acessa somente os produtos do usuário
            .orderBy(FIELD_NAME, Query.Direction.ASCENDING)                                         // Ordenação dos produtos
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->                     // Retorna uma lista "querySnapshot". A "addSnapshotListener" é uma operação assíncrona com o Firestore
                if (firebaseFirestoreException != null) {                                           // Tratamento da excessão
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val products = ArrayList<Product>()
                    querySnapshot.forEach {                                                         // it contém os objetos da lista
                        val product = it.toObject<Product>()
                        product.id = it.id
                        products.add(product)
                    }
                    liveProducts.postValue(products)
                } else {
                    Log.d(TAG, "No product has been found")
                }
            }
        return liveProducts
    }

    // Busca o produto pelo seu código
    // code: String  -> parametro de entrada
    // MutableLiveData<Product>  -> parametro de saida
    fun getProductByCode(code: String): MutableLiveData<Product> {
        val liveProduct: MutableLiveData<Product> = MutableLiveData()
        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_CODE, code)
            .whereEqualTo(FIELD_USER_ID, firebaseAuth.uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val products = ArrayList<Product>()
                    querySnapshot.forEach {
                        val product = it.toObject<Product>()
                        product.id = it.id
                        products.add(product)
                    }
                    liveProduct.postValue(products[0])                      // Pega somente um produto ao invés de uma lista
                } else {
                    Log.d(TAG, "No product has been found")
                }
            }
        return liveProduct
    }
}