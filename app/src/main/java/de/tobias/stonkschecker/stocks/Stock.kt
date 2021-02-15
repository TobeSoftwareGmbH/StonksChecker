package de.tobias.stonkschecker.stocks

import org.json.JSONArray
import org.json.JSONObject

class Stock (
    val name: String, val historicStockData: List<HistoricStockData>,
    val previousClosingPriceOneTradingDayAgo: Double, val lastPrice: Double
) {

    companion object {
        const val STONKS = 1
        const val NOT_STONKS = -1
        const val CONFUSED_STONKS = 0

        fun getStockData(jsonArray: JSONArray) : Stock {
            val stockData: JSONObject = jsonArray.getJSONObject(0);

            val name: String = stockData.getString("id")
            val priceHistory: JSONArray = stockData.getJSONArray("price")

            val priceHistoryData: ArrayList<HistoricStockData> = HistoricStockData.parseStockData(priceHistory)

            return Stock(name, priceHistoryData, stockData.getDouble("previousClosingPriceOneTradingDayAgo"), stockData.getDouble("lastPrice"))
        }
    }

    fun getStonkState() : Int {
        val priceChange = lastPrice - previousClosingPriceOneTradingDayAgo;

        if (priceChange > 2) return STONKS
        else if (priceChange < -2) return NOT_STONKS
        else return CONFUSED_STONKS
    }

}