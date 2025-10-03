package com.codepath.lab6

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ParksAdapter(
    private val context: Context,
    private val items: List<Park>
) : RecyclerView.Adapter<ParksAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.parkImage)
        val name: TextView = view.findViewById(R.id.parkName)
        val desc: TextView = view.findViewById(R.id.parkDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_park, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.name.text = p.fullName ?: "Unnamed Park"
        holder.desc.text = p.description ?: ""

        val url = p.imageUrl
        if (!url.isNullOrBlank()) {
            Glide.with(context).load(url).into(holder.image)
        } else {
            holder.image.setImageDrawable(null)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(NavExtras.PARK_EXTRA, p)  // <- send the Park
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}


