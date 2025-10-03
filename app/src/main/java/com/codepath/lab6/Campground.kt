package com.codepath.lab6

import android.support.annotation.Keep
import org.json.JSONObject
import java.io.Serializable

@Keep
data class Campground(
    val name: String?,
    val description: String?,
    val latLong: String?,        // <-- add this
    val imageUrl: String?
) : Serializable {
    companion object {
        fun fromJson(obj: JSONObject): Campground {
            val name = obj.optString("name", null)
            val description = obj.optString("description", null)
            val latLong = obj.optString("latLong", null)

            var img: String? = null
            val images = obj.optJSONArray("images")
            if (images != null && images.length() > 0) {
                img = images.optJSONObject(0)?.optString("url", null)
            }

            return Campground(
                name = name,
                description = description,
                latLong = latLong,
                imageUrl = img
            )
        }
    }
}
