package br.com.siecola.androidproject04.product

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import br.com.siecola.androidproject04.R
import br.com.siecola.androidproject04.databinding.FragmentProductDetailBinding
import br.com.siecola.androidproject04.databinding.FragmentProductsListBinding
import com.google.firebase.analytics.FirebaseAnalytics

private const val TAG = "ProductsListFragment"

class ProductsListFragment : Fragment() {

    private val productListViewModel: ProductListViewModel by lazy {
        ViewModelProviders.of(this).get(ProductListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentProductsListBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        binding.productListViewModel = productListViewModel

        val itemDecor = DividerItemDecoration(getContext(), VERTICAL);

        binding.rcvProducts.addItemDecoration(itemDecor);

        binding.rcvProducts.adapter = ProductAdapter(ProductAdapter.ProductClickListener {
            Log.i(TAG, "Product selected: ${it.name}")
            this.findNavController()
                .navigate(ProductsListFragmentDirections.actionShowProductDetail(it.code!!))
        })

        binding.fab.setOnClickListener { view ->
            this.findNavController()
                .navigate(ProductsListFragmentDirections.actionShowProductDetail(null))
                // amagalhaes
                val firebaseAnalytics = FirebaseAnalytics.getInstance(this.context!!)
                firebaseAnalytics.logEvent("new_item", null)
        }

        return binding.root
    }
}