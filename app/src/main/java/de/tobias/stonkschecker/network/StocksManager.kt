package de.tobias.stonkschecker.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class StocksManager(context: Context) {
    val queue : RequestQueue

    init {
        queue = Volley.newRequestQueue(context)
    }

    fun getSearchURL(query: String) : String {
        return "https://search.bloomberg.com/lookup.json?types=Company_Public,Index,Fund,Currency,Commodity,Bond,Person,Author,Topic&exclude_subtypes=label:editorial&group_size=3,3,3,3,3,3,3,3,6&fields=name,slug,ticker_symbol,url,organization,title,primary_site&highlight=1&query=$query"
    }

    fun getStockURL(stockName: String) : String {
        return "https://www.bloomberg.com/markets/api/bulk-time-series/price/$stockName?timeFrame=1_DAY"
    }

    fun getJSONResponse(url: String, networkCallback: NetworkCallback): NetworkCallback {
        //Example response https://www.bloomberg.com/markets/api/bulk-time-series/price/GME%3AUS?timeFrame=1_DAY
        //Search https://search.bloomberg.com/lookup.json?types=Company_Public,Index,Fund,Currency,Commodity,Bond,Person,Author,Topic&exclude_subtypes=label:editorial&group_size=3,3,3,3,3,3,3,3,6&fields=name,slug,ticker_symbol,url,organization,title,primary_site&highlight=1&query=GameStop
        val stockRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                networkCallback.onFinished(JSONArray(response))
            },
            { error -> networkCallback.onError(error) })

        queue.add(stockRequest)
        return networkCallback
    }
}