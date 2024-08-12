package com.example.basedatos


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ItemAdapter(private val itemList: List<Pair<String, String>>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.textViewItem)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val (itemId, itemText) = itemList[position]
        holder.itemTextView.text = itemText

        holder.deleteButton.setOnClickListener {
            // Eliminar el item de Firebase
            database.child("items").child(itemId).removeValue()
        }
    }

    override fun getItemCount() = itemList.size
}

