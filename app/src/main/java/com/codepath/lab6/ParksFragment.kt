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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers

class ParksFragment : Fragment() {

    private val parks = mutableListOf<Park>()
    private lateinit var parksRecyclerView: RecyclerView
    private lateinit var parksAdapter: ParksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_parks, container, false)
        parksRecyclerView = view.findViewById(R.id.parks)
        parksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        parksRecyclerView.setHasFixedSize(true)
        parksAdapter = ParksAdapter(requireContext(), parks)
        parksRecyclerView.adapter = parksAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchParks()
    }

    private fun fetchParks() {
        val apiKey = BuildConfig.API_KEY
        val url = "https://developer.nps.gov/api/v1/parks?api_key=$apiKey"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("ParksFragment", "Failed to fetch parks: $statusCode $response", throwable)
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i("ParksFragment", "API raw: ${json.jsonObject}")
                try {
                    val parsed = Json { ignoreUnknownKeys = true }
                        .decodeFromString<ParksResponse>(json.jsonObject.toString())

                    Log.i("ParksFragment", "Parsed count = ${parsed.data?.size}")

                    parks.clear()
                    parks.addAll(parsed.data ?: emptyList())
                    parksAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.e("ParksFragment", "Serialization error", e)
                }
            }

        })
    }

    companion object {
        fun newInstance() = ParksFragment()
    }
}
