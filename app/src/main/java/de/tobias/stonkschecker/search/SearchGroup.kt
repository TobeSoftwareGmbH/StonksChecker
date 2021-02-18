package de.tobias.stonkschecker.search

import org.json.JSONArray
import org.json.JSONObject

class SearchGroup (val category: Int, val results: ArrayList<SearchResult>) {
    companion object {
        const val COMPANY_PUBLIC = 0
        const val INDEX = 1
        const val FUND = 2
        const val CURRENCY = 3
        const val COMMODITY = 4
        const val BOND = 5

        fun parseSearchGroup(jsonObject: JSONObject, category: Int) : SearchGroup {
            var resultsList: ArrayList<SearchResult> = ArrayList()

            val results: JSONArray = jsonObject.getJSONArray("results")

            for(i in 0 until results.length()) {
                val result: JSONObject = results.getJSONObject(i)
                resultsList.add(SearchResult.fromValues(result.getString("name"), result.getString("ticker_symbol"), result.getDouble("score")))
            }

            return SearchGroup(category, resultsList)
        }
    }
}