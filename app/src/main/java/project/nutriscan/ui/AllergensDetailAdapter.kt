package project.nutriscan.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import project.nutriscan.R
import project.nutriscan.databinding.ItemAllergenDetailBinding
import project.nutriscan.model.AllergenInfo

class AllergensDetailAdapter : ListAdapter<AllergenInfo, AllergensDetailAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemAllergenDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllergenInfo) {
            binding.allergenCode.text = item.code
            binding.allergenName.text = item.fullName
            binding.allergenCategory.text = item.category
            binding.allergenRiskLevel.text = item.riskLevel
            binding.allergenDescription.text = item.description
            binding.allergenSymptoms.text = item.symptoms
            binding.allergenAvoidance.text = item.avoidanceAdvice


            // Color code for risk level
            val riskColor = when (item.riskLevel) {
                "HIGH RISK" -> ContextCompat.getColor(binding.root.context, R.color.risk_high)
                "MEDIUM RISK" -> ContextCompat.getColor(binding.root.context, R.color.risk_medium)
                "LOW RISK" -> ContextCompat.getColor(binding.root.context, R.color.risk_low)
                "VARIABLE RISK" -> ContextCompat.getColor(binding.root.context, R.color.risk_variable)
                else -> ContextCompat.getColor(binding.root.context, R.color.risk_unknown)
            }
            binding.allergenRiskLevel.setTextColor(riskColor)

            // Set card stroke color based on risk
            binding.allergenCard.strokeColor = riskColor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAllergenDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<AllergenInfo>() {
        override fun areItemsTheSame(oldItem: AllergenInfo, newItem: AllergenInfo): Boolean =
            oldItem.code == newItem.code

        override fun areContentsTheSame(oldItem: AllergenInfo, newItem: AllergenInfo): Boolean =
            oldItem == newItem
    }
}
