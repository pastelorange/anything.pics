package com.drestation.anythingpics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

// This class acts as middleware for the model and view
class PinAdapter(
    private val pins: List<Pin>,
    private val itemListener: ItemListener
) : RecyclerView.Adapter<PinAdapter.ViewHolder>() {

    // This class is used to access the elements in item_pin layout file
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById<TextView>(R.id.titleTextView)
        val imageView: ImageView = itemView.findViewById<ImageView>(R.id.imageView)
    }

    // This renders each item_pin
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pin, parent, false)
        return ViewHolder(view)
    }

    // This binds the data from the pins list with the view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pin = pins[position]
        with(viewHolder) {
            titleTextView.text = pin.title
            Picasso.get().load(pin.imageUrl).into(imageView)

            itemView.setOnClickListener {
                itemListener.itemSelected(pin)
            }
        }
    }

    // Custom interface for selecting pins
    interface ItemListener {
        fun itemSelected(pin: Pin)
    }

    // Unused but need to inherit from parent class
    override fun getItemCount(): Int {
        return pins.size
    }
}