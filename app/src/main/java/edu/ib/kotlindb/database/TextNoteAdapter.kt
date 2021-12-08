package edu.ib.kotlindb.database

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.ib.kotlindb.R
import edu.ib.kotlindb.activities.AllNotesActivity
import edu.ib.kotlindb.activities.TextNotesActivity
import edu.ib.kotlindb.models.TextNote

class TextNotesAdapter(val context: Context, private val items: ArrayList<TextNote>) :
    RecyclerView.Adapter<TextNotesAdapter.ViewHolder>() {
    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val layout: LinearLayout = view.findViewById(R.id.linear_rows)

        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        var note: CardView = view.findViewById(R.id.item)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_text_notes_row,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.tvTitle.text = item.title

        // Updating the background color according to the odd/even positions in list.

        holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))

        holder.ivDelete.setOnClickListener { view ->

            if (context is TextNotesActivity) {
                context.deleteRecordAlertDialog(item)
            }


        }
        holder.note.setOnClickListener {
            if (context is TextNotesActivity) {
//                context.deleteRecordAlertDialog(item)
                context.showDetailsOfTextNote(item)

            }

        }


    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }


}