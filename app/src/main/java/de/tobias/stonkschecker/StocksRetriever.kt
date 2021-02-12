package de.tobias.stonkschecker

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class StocksRetriever(context: Context) {
    val queue : RequestQueue

    init {
        queue = Volley.newRequestQueue(context)
    }


    fun getStockInfo(stockName: String): Unit {
        //Example response https://www.bloomberg.com/markets/api/bulk-time-series/price/GME%3AUS?timeFrame=1_DAY
        val url = "https://www.bloomberg.com/markets/api/bulk-time-series/price/$stockName%3AUS?timeFrame=1_DAY";

        val stockRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.v("StockInfo", "Response is: ${response.substring(0, 500)}");
            },
            { Log.e("StockInfo", "That didn't work!") })

        queue.add(stockRequest)
    }
}