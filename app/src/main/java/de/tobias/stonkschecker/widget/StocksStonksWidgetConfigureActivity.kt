package de.tobias.stonkschecker.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import de.tobias.stonkschecker.R
import de.tobias.stonkschecker.adapters.SearchResultRecyclerViewAdapter
import de.tobias.stonkschecker.network.NetworkCallback
import de.tobias.stonkschecker.network.NetworkManager
import de.tobias.stonkschecker.search.SearchResult
import de.tobias.stonkschecker.search.SearchResults
import org.json.JSONArray


/**
 * The configuration screen for the [StocksStonksWidget] AppWidget.
 */
class StocksStonksWidgetConfigureActivity : NetworkCallback, SearchResultRecyclerViewAdapter.SearchResultItemClickListener,  AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private lateinit var widgetStockName: EditText
    private val searchResultRecyclerViewAdapter: SearchResultRecyclerViewAdapter = SearchResultRecyclerViewAdapter(this, this)



    public override fun onCreate(icicle: Bundle?) {
        setTheme(R.style.AppTheme_WidgetConfiguration)
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setContentView(R.layout.stocks_stonks_widget_configure)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        title = getString(R.string.title_configure)

        //appWidgetText.setText(loadTitlePref(this@StocksStonksWidgetConfigureActivity, appWidgetId))
    }

    override fun onStart() {
        super.onStart()

        //Setup the recyclerView
        val recyclerView : RecyclerView = findViewById(R.id.searchResultList)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = searchResultRecyclerViewAdapter

        //Initialise the networkManager class
        val networkManager  = NetworkManager(this)

        //Set up EditText listener and make the keyboard pop up by requesting focus for it
        widgetStockName = findViewById<View>(R.id.appwidget_text) as EditText
        widgetStockName.requestFocus()
        widgetStockName.setOnEditorActionListener(TextView.OnEditorActionListener() //called when the user hits the search button on their keyboard
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

    override fun onError(error: VolleyError) {
        TODO("Not yet implemented")
    }

    //Called by the RecyclerView.Adapter when the user selects and item in the results list
    override fun onListItemClick(searchResult: SearchResult) {
        WidgetManager.saveStockData(this, appWidgetId, searchResult.ticker_symbol, searchResult.name)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(this)
        StocksStonksWidget().updateAppWidget(this, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId in our intent
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    private fun hideKeyboard() {
        val view: View = currentFocus ?: View(this)
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
