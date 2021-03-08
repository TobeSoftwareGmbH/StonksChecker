package de.tobias.stonkschecker.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.google.android.material.textfield.TextInputEditText
import de.tobias.stonkschecker.R
import de.tobias.stonkschecker.adapters.SearchResultRecyclerViewAdapter
import de.tobias.stonkschecker.network.NetworkCallback
import de.tobias.stonkschecker.network.NetworkManager
import de.tobias.stonkschecker.search.SearchResult
import de.tobias.stonkschecker.search.SearchResults
import org.json.JSONArray

class StockSearchActivity : AppCompatActivity(), NetworkCallback, SearchResultRecyclerViewAdapter.SearchResultItemClickListener {

    private lateinit var widgetStockName: EditText
    private val searchResultRecyclerViewAdapter: SearchResultRecyclerViewAdapter = SearchResultRecyclerViewAdapter(
        this,
        this
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_search)

        title = getString(R.string.title_configure)

        //Setup the recyclerView
        val recyclerView : RecyclerView = findViewById(R.id.searchResultList)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = searchResultRecyclerViewAdapter

        //Initialise the networkManager class
        val networkManager  = NetworkManager(this)

        //Set up EditText listener and make the keyboard pop up by requesting focus for it
        widgetStockName = findViewById<TextInputEditText>(R.id.stock_name_input)
        widgetStockName.requestFocus()
        widgetStockName.setOnEditorActionListener(
            TextView.OnEditorActionListener() //called when the user hits the search button on their keyboard
        { _: TextView, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //Hide the keyboard
                hideKeyboard()

                //Display Progressbar
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

                //Send request
                networkManager.getJSONResponse(
                    NetworkManager.getSearchURL(widgetStockName.text.toString()),
                    this
                )
            }
            return@OnEditorActionListener false
        })
    }

    //The network request has been returned by Volley
    override fun onFinished(jsonArray: JSONArray) {
        //Hide Progressbar
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

        //Show results
        runOnUiThread { searchResultRecyclerViewAdapter.overrideSearchResults(
            SearchResults.parseSearchResponse(
                jsonArray
            ).searchItems
        ) }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onError(error: VolleyError) {
        TODO("Not yet implemented")
    }

    //Called by the RecyclerView.Adapter when the user selects and item in the results list
    override fun onListItemClick(searchResult: SearchResult) {
        val data = Intent()
        data.putExtra("ticker", searchResult.ticker_symbol)
        data.putExtra("name", searchResult.name)

        setResult(Activity.RESULT_OK, data)
        super.onBackPressed() //Simulating a back-button press, since finish() would not show the slide animation
    }

    private fun hideKeyboard() {
        val view: View = currentFocus ?: View(this)
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}