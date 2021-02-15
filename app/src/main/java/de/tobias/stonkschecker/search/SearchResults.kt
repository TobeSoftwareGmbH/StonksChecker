package de.tobias.stonkschecker.search

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class SearchResults (val searchGroup: ArrayList<SearchGroup>){

    companion object {
        fun parseSearchResponse(jsonArray: JSONArray) : SearchResults {
            var categories : ArrayList<SearchGroup> = ArrayList()

            for(i in 0 until jsonArray.length()) {
                val category: JSONObject = jsonArray.getJSONObject(i)
                categories.add(SearchGroup.parseSearchGroup(category, i))
            }

            return SearchResults(categories)
        }
    }

    override fun toString(): String {
        for (i in searchGroup.indices) {
            val group : SearchGroup = searchGroup.get(i)
            val results : ArrayList<SearchResult> = group.results
            Log.v("SEARCH", group.category.toString())

            for (j in results.indices) {
                val result: SearchResult = results[j]
                Log.v(result.ticker_symbol, result.name)
            }
        }
        return ""
    }
}