package de.tobias.stonkschecker.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.tobias.stonkschecker.R
import de.tobias.stonkschecker.search.SearchResult

class SearchResultRecyclerViewAdapter(val searchResultItemClickListener: SearchResultItemClickListener, val context: Context): RecyclerView.Adapter<SearchResultRecyclerViewAdapter.SearchResultViewHolder>() {
    private var results: ArrayList<SearchResult> = ArrayList()
    private var delay = 0
    private val delayModifier = 50

    private val animatedView: ArrayList<Int> = ArrayList()

    inner class SearchResultViewHolder(private val view: View) : View.OnClickListener, RecyclerView.ViewHolder(view) {

        override fun onClick(p0: View?) {
            val position = adapterPosition
            searchResultItemClickListener.onListItemClick(results[position])
        }
        init {
            view.setOnClickListener(this)
        }

        fun animateView() {
            val itemAniation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down)
            itemAniation.startOffset = delay.toLong()

            view.startAnimation(itemAniation)
            delay += delayModifier
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        //Log.v("RecyclerViewManager", "onCreateViewHolder called")
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return SearchResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        //Log.v("RecyclerViewManager", "onBindViewHolder called")
        val view: View = holder.itemView
        val result: SearchResult = results[position]

        (view.findViewById(R.id.id) as TextView ).text = result.ticker_symbol
        (view.findViewById(R.id.current_value) as TextView ).text = result.value.toString()
        (view.findViewById(R.id.name) as TextView ).text = result.name

        if(!animatedView.contains(position)) {
            holder.animateView()
            animatedView.add(position)
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun overrideSearchResults(newResults: ArrayList<SearchResult>) {
        delay = 0
        results.clear()
        results.addAll(newResults)

        animatedView.clear()

        notifyDataSetChanged()
        Log.v("RecyclerView", "New result size is: "+newResults.size)
    }

    interface SearchResultItemClickListener {
        fun onListItemClick(searchResult: SearchResult)
    }
}