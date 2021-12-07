package edu.ib.kotlindb.fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import edu.ib.kotlindb.databinding.FragmentItemBinding
import edu.ib.kotlindb.fragment.placeholder.PlaceholderContent


import edu.ib.kotlindb.models.TextNote

/**
 * [RecyclerView.Adapter] that can display a [TextNote].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTextNotesRecyclerViewAdapter(
    private val values: MutableList<PlaceholderContent.TextNote>
) : RecyclerView.Adapter<MyTextNotesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
//        holder.idView.text = item.title
//        holder.contentView.text = item.desc
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}