package de.tobias.stonkschecker.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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
class StocksStonksWidgetConfigureActivity : NetworkCallback, SearchResultRecyclerViewAdapter.SearchResultItemClickListener,  Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private lateinit var widgetStockName: EditText
    val searchResultRecyclerViewAdapter: SearchResultRecyclerViewAdapter = SearchResultRecyclerViewAdapter(this)



    public override fun onCreate(icicle: Bundle?) {
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

        //appWidgetText.setText(loadTitlePref(this@StocksStonksWidgetConfigureActivity, appWidgetId))
    }

    override fun onStart() {
        super.onStart()

        val recyclerView : RecyclerView = findViewById(R.id.searchResultList)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = searchResultRecyclerViewAdapter

        val networkManager : NetworkManager = NetworkManager(this)

        widgetStockName = findViewById<View>(R.id.appwidget_text) as EditText
        widgetStockName.requestFocus()
        widgetStockName.setOnEditorActionListener(TextView.OnEditorActionListener()
        { textview: TextView, actionId: Int, keyevent: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //Hide the keyboard
                hideKeyboard()

                //Display Progressbar
                (this.findViewById(R.id.progressBar) as ProgressBar).visibility = View.VISIBLE

                //Send request
                networkManager.getJSONResponse(
                    NetworkManager.getSearchURL(widgetStockName.text.toString()),
                    this
                )
            }
            return@OnEditorActionListener false
        })
    }

    override fun onFinished(jsonArray: JSONArray) {
        //Hide Progressbar
        (this.findViewById(R.id.progressBar) as ProgressBar).visibility = View.GONE

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

    override fun onListItemClick(searchResult: SearchResult) {
        saveStockData(this, appWidgetId, searchResult.ticker_symbol, searchResult.name)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(this)
        StocksStonksWidget().updateAppWidget(this, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    fun Context.hideKeyboard() {
        val view: View = currentFocus ?: View(this)
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

private const val PREFS_NAME = "de.tobias.stonkschecker.StocksStonksWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveStockData(context: Context, appWidgetId: Int, ticker_symbol: String, name: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, ticker_symbol)
    prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_name", name)
    prefs.apply()
}

internal fun getTickerSymbol(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getString(PREF_PREFIX_KEY + appWidgetId, "")!!
}

internal fun getStockName(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getString(PREF_PREFIX_KEY + appWidgetId + "_name", "")!!
}

internal fun deleteStockData(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.remove(PREF_PREFIX_KEY + appWidgetId + "_name")
    prefs.apply()
}

internal fun isInitialised (context: Context, appWidgetId: Int) : Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.contains(PREF_PREFIX_KEY + appWidgetId + "_name")
}