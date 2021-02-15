package de.tobias.stonkschecker.stocks

import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoricStockData(date: Date, value: Double) {
    val date: Date =  date
    val value: Double = value

    companion object {
        fun parseStockData(prices: JSONArray) : ArrayList<HistoricStockData> {
            var historicStockData: ArrayList<HistoricStockData> = ArrayList()
            for (i in 0 until prices.length()) {
                //Get Object
                val data: JSONObject = prices.getJSONObject(i)

                //Get Stock Value
                val value: Double = data.getDouble("value")

                //Get Date-Time String
                val dateTime: String = data.getString("dateTime")

                //We now need to format it into an usable form. Format is yyyy-mm-ddThh:mm:ssZ
                //TODO take timezone into account
                val date: Date = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault()).parse(dateTime);

                historicStockData.add(HistoricStockData(date, value))

                //Log.v(date.toString(), value.toString())
            }
            return historicStockData
        }


    }
}