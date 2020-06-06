package br.com.siecola.androidproject04.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.siecola.androidproject04.persistence.Product
import br.com.siecola.androidproject04.databinding.ItemProductBinding
import com.google.firebase.analytics.FirebaseAnalytics



class ProductAdapter(val onProductClickListener: ProductClickListener) :

    ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiff) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {

        firebaseAnalytics = FirebaseAnalytics.getInstance(parent.context)

        return ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position : Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, product.code)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
            onProductClickListener.onClick(product)
        }

        holder.itemView.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, product.code)
            firebaseAnalytics.logEvent("attempt_delete_product", bundle)
            true
        }
    }

    class ProductViewHolder(private var binding: ItemProductBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    companion object ProductDiff : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return ((oldItem.id == newItem.id)
                    && (oldItem.name.equals(newItem.name))
                    && (oldItem.code.equals(newItem.code))
                    && (oldItem.price == newItem.price))

        }
    }

    class ProductClickListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }

}
