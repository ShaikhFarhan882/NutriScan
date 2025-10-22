package project.nutriscan.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import project.nutriscan.R
import project.nutriscan.database.SavedProduct
import project.nutriscan.databinding.ItemSavedProductBinding
import java.text.SimpleDateFormat
import java.util.*

class SavedProductAdapter : RecyclerView.Adapter<SavedProductAdapter.SavedProductViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<SavedProduct>() {
        override fun areItemsTheSame(oldItem: SavedProduct, newItem: SavedProduct): Boolean {
            return oldItem.barcode == newItem.barcode
        }

        override fun areContentsTheSame(oldItem: SavedProduct, newItem: SavedProduct): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var savedProducts: List<SavedProduct>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class SavedProductViewHolder(private val binding: ItemSavedProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: SavedProduct) {
            binding.apply {
                // Product info
                productName.text = product.productName ?: "Unknown Product"
                brandName.text = product.brands ?: "Unknown Brand"

                // Product image
                productImage.load(product.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_profile_24)
                    error(R.drawable.ic_palm_oil_caution)
                }

                //Palm oil status - use hasPalmOil flag
                when {
                    product.hasPalmOil -> {
                        // Contains palm oil
                        palmOilBadge.visibility = View.VISIBLE
                        palmOilIcon.setImageResource(R.drawable.ic_palm_oil_warning)
                        palmOilIcon.setColorFilter(Color.parseColor("#D32F2F"))
                        palmOilText.text = "Contains Palm Oil"
                        palmOilText.setTextColor(Color.parseColor("#D32F2F"))

                        // Show sustainable icon if applicable
                        sustainableIcon.visibility = if (product.isSustainable == true) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }

                    (product.mayContainPalmOilCount ?: 0) > 0 -> {
                        // May contain palm oil
                        palmOilBadge.visibility = View.VISIBLE
                        palmOilIcon.setImageResource(R.drawable.ic_palm_oil_caution)
                        palmOilIcon.setColorFilter(Color.parseColor("#F57C00"))
                        palmOilText.text = "May Contain"
                        palmOilText.setTextColor(Color.parseColor("#F57C00"))
                        sustainableIcon.visibility = View.GONE
                    }

                    else -> {
                        // Palm oil free
                        palmOilBadge.visibility = View.VISIBLE
                        palmOilIcon.setImageResource(R.drawable.ic_palm_oil_free)
                        palmOilIcon.setColorFilter(Color.parseColor("#388E3C"))
                        palmOilText.text = "Palm Oil Free"
                        palmOilText.setTextColor(Color.parseColor("#388E3C"))
                        sustainableIcon.visibility = View.GONE
                    }
                }



                // Saved date
                savedDate.text = formatSavedDate(product.savedAt)

                // Click listeners
                root.setOnClickListener { onItemClickListener?.invoke(product) }
                deleteButton.setOnClickListener { onDeleteClickListener?.invoke(product) }
            }
        }

        private fun formatSavedDate(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < 60000 -> "Just now"
                diff < 3600000 -> "${diff / 60000} min ago"
                diff < 86400000 -> "${diff / 3600000} hrs ago"
                diff < 604800000 -> "${diff / 86400000} days ago"
                else -> {
                    val date = Date(timestamp)
                    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedProductViewHolder {
        return SavedProductViewHolder(
            ItemSavedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SavedProductViewHolder, position: Int) {
        holder.bind(savedProducts[position])
    }

    override fun getItemCount() = savedProducts.size

    private var onItemClickListener: ((SavedProduct) -> Unit)? = null
    private var onDeleteClickListener: ((SavedProduct) -> Unit)? = null

    fun setOnItemClickListener(listener: (SavedProduct) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnDeleteClickListener(listener: (SavedProduct) -> Unit) {
        onDeleteClickListener = listener
    }
}
