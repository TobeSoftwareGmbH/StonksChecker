package de.tobias.stonkschecker.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import de.tobias.stonkschecker.R
import de.tobias.stonkschecker.adapters.SearchResultRecyclerViewAdapter
import de.tobias.stonkschecker.network.NetworkCallback
import de.tobias.stonkschecker.network.NetworkManager
import de.tobias.stonkschecker.search.SearchResults
import org.json.JSONArray


/**
 * The configuration screen for the [StocksStonksWidget] AppWidget.
 */
class StocksStonksWidgetConfigureActivity : NetworkCallback, Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private lateinit var widgetStockName: EditText
    val searchResultRecyclerViewAdapter: SearchResultRecyclerViewAdapter = SearchResultRecyclerViewAdapter()


    private var onClickListener = View.OnClickListener {
        val context = this@StocksStonksWidgetConfigureActivity

        // When the button is clicked, store the string locally
        val widgetText = widgetStockName.text.toString()
        saveTitlePref(context, appWidgetId, widgetText)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }



    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setContentView(R.layout.stocks_stonks_widget_configure)
        findViewById<View>(R.id.add_button).setOnClickListener(onClickListener)

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
        widgetStockName.setOnEditorActionListener(TextView.OnEditorActionListener()
        { textview: TextView, actionId: Int, keyevent: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                networkManager.getJSONResponse(
                    NetworkManager.getSearchURL(widgetStockName.text.toString()),
                    this
                )
            }
            return@OnEditorActionListener false
        })
    }

    override fun onFinished(jsonArray: JSONArray) {
        runOnUiThread { searchResultRecyclerViewAdapter.overrideSearchResults(
            SearchResults.parseSearchResponse(
                jsonArray
            ).searchItems
        ) }
    }

    override fun onError(error: VolleyError) {
        TODO("Not yet implemented")
    }

}

private const val PREFS_NAME = "de.tobias.stonkschecker.StocksStonksWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    return titleValue ?: context.getString(R.string.appwidget_text)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}