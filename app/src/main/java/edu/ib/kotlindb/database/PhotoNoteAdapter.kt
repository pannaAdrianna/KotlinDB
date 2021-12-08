package edu.ib.kotlindb.database


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.ib.kotlindb.R
import edu.ib.kotlindb.activities.AllNotesActivity
import edu.ib.kotlindb.models.PhotoNote

class PhotoNoteAdapter(val context: Context, private val items: ArrayList<PhotoNote>) :
    RecyclerView.Adapter<PhotoNoteAdapter.ViewHolder>() {
    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val layout: LinearLayout = view.findViewById(R.id.linear_row_item_note)

        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var note: CardView = view.findViewById(R.id.item)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoNoteAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_row_item_note,
                parent,
                false
            )
        )
    }


    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PhotoNoteAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.tvTitle.text = item.title

        // Updating the background color according to the odd/even positions in list.

        holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))




        holder.note.setOnClickListener {
            if (context is AllNotesActivity) {
                context.showDetailsOfPhotoNote(item)
//                context.deleteRecordAlertDialog(item)
//                context.showDetails(item)

            }

        }

    }


}