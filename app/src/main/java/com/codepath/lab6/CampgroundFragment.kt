package com.codepath.lab6

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class CampgroundFragment : Fragment() {

    private val camps = mutableListOf<Campground>()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CampgroundAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_campground, container, false)
        recycler = view.findViewById(R.id.campgrounds)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setHasFixedSize(true)

        adapter = CampgroundAdapter(requireContext(), camps)
        recycler.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCampgrounds()
    }

    private fun fetchCampgrounds() {
        val apiKey = com.codepath.lab6.BuildConfig.API_KEY   // <-- fully qualified to avoid the wrong import
        val url = "https://developer.nps.gov/api/v1/campgrounds?api_key=$apiKey"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("CampgroundFragment", "Failed to fetch: $statusCode $response", throwable)
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i("CampgroundFragment", "API raw: ${json.jsonObject}")
                try {
                    val data = json.jsonObject.getJSONArray("data")
                    Log.i("CampgroundFragment", "Fetched count = ${data.length()}")
                    camps.clear()
                    for (i in 0 until data.length()) {
                        val obj = data.getJSONObject(i)
                        camps.add(Campground.fromJson(obj))
                    }
                    adapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Log.e("CampgroundFragment", "JSON error", e)
                }
            }

        })
    }

    companion object { fun newInstance() = CampgroundFragment() }
}
