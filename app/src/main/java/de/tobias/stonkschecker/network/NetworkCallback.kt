package de.tobias.stonkschecker.network

import org.json.JSONArray

interface NetworkCallback {
    fun onFinished(jsonArray: JSONArray)
}