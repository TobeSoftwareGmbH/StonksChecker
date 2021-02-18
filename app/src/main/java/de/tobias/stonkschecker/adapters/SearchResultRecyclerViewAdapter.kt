package de.tobias.stonkschecker.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.tobias.stonkschecker.R
import de.tobias.stonkschecker.search.SearchResult

class SearchResultRecyclerViewAdapter: RecyclerView.Adapter<SearchResultRecyclerViewAdapter.SearchResultViewHolder>() {
    private var results: ArrayList<SearchResult> = ArrayList()

    init {
        results.add(SearchResult("name", ",", 69420.0))
        notifyDataSetChanged()
    }

    class SearchResultViewHolder(view: View) :RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        Log.v("RecyclerViewManager", "onCreateViewHolder called")
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return SearchResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        Log.v("RecyclerViewManager", "onBindViewHolder called")
        val view: View = holder.itemView
        val result: SearchResult = results[position]

        (view.findViewById(R.id.id) as TextView ).text = result.ticker_symbol
        (view.findViewById(R.id.current_value) as TextView ).text = result.value.toString()
        (view.findViewById(R.id.name) as TextView ).text = result.name
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun overrideSearchResults(newResults: ArrayList<SearchResult>) {
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
        Log.v("RecyclerView", "New result size is: "+newResults.size)
    }
}