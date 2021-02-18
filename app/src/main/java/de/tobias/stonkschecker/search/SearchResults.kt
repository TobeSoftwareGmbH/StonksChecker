package de.tobias.stonkschecker.search

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class SearchResults (val searchGroup: ArrayList<SearchGroup>, val searchItems: ArrayList<SearchResult>){

    companion object {
        fun parseSearchResponse(jsonArray: JSONArray) : SearchResults {
            val categories : ArrayList<SearchGroup> = ArrayList()
            val searchItems: ArrayList<SearchResult> = ArrayList()

            for(i in 0 until jsonArray.length()) {
                val category: JSONObject = jsonArray.getJSONObject(i)
                categories.add(SearchGroup.parseSearchGroup(category, i))
            }

            for (i in categories.indices) {
                val group : SearchGroup = categories.get(i)
                val results : ArrayList<SearchResult> = group.results

                for (j in results.indices) {
                    searchItems.add(results[j])
                }
            }

            return SearchResults(categories, searchItems)
        }
    }

    override fun toString(): String {
        for (i in searchGroup.indices) {
            val group : SearchGroup = searchGroup.get(i)
            val results : ArrayList<SearchResult> = group.results
            Log.v("SEARCH", group.category.toString())

            for (j in results.indices) {
                val result: SearchResult = results[j]
                Log.v(result.ticker_symbol, result.name + " " + result.value)
            }
        }
        return ""
    }
}