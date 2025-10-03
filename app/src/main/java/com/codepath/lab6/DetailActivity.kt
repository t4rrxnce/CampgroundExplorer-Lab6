package com.codepath.lab6

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val image: ImageView = findViewById(R.id.detailImage)
        val title: TextView = findViewById(R.id.detailTitle)
        val description: TextView = findViewById(R.id.detailDescription)

        // Try Park first
        val park = intent.getSerializableExtra(NavExtras.PARK_EXTRA) as? Park

        // (Optional) If you later wire Campgrounds, you can pass a Campground model and read it here:
        // val campground = intent.getSerializableExtra(NavExtras.CAMPGROUND_EXTRA) as? Campground

        when {
            park != null -> {
                title.text = park.fullName ?: "Park"
                description.text = park.description ?: ""
                park.imageUrl?.let { Glide.with(this).load(it).into(image) } ?: image.setImageDrawable(null)
            }
            else -> {
                title.text = "Details"
                description.text = ""
                image.setImageDrawable(null)
            }
        }
    }
}
