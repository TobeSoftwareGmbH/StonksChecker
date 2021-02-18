package de.tobias.stonkschecker.network

import com.android.volley.VolleyError
import org.json.JSONArray

interface NetworkCallback {
    fun onFinished(jsonArray: JSONArray)
    fun onError(error: VolleyError)
}