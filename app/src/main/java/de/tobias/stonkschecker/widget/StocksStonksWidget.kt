package de.tobias.stonkschecker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.android.volley.VolleyError
import de.tobias.stonkschecker.R
import de.tobias.stonkschecker.network.NetworkCallback
import de.tobias.stonkschecker.network.NetworkManager
import de.tobias.stonkschecker.stocks.Stock
import org.json.JSONArray


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [StocksStonksWidgetConfigureActivity]
 */
class StocksStonksWidget : NetworkCallback, AppWidgetProvider() {
    private lateinit var context: Context
    private lateinit var appWidgetManager: AppWidgetManager

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteStockData(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onFinished(jsonArray: JSONArray) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.stocks_stonks_widget)

        //Parse stock and get stonk State
        val stock: Stock = Stock.getStockData(jsonArray)
        val stonkState: Int = stock.getStonkState()

        //Get appWidgetId
        val appWidgetId = getWidgetIdFromTickerSymbol(context, stock.name)

        //Update Layout text
        views.setViewVisibility(R.id.widget_loading, View.GONE)
        views.setTextViewText(R.id.stock_name, getStockName(context, appWidgetId) + ":")

        //Set image accordingly
        if(stonkState == Stock.STONKS) {
            views.setImageViewResource(R.id.img_stonks, R.drawable.stonks)
        } else if (stonkState == Stock.NOT_STONKS) {
            views.setImageViewResource(R.id.img_stonks, R.drawable.not_stonks)
        } else {
            views.setImageViewResource(R.id.img_stonks, R.drawable.confused_stonks)
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onError(error: VolleyError) {
        TODO("Not yet implemented")
    }

    private fun getPendingSelfIntent(context: Context, appWidgetId: Int): PendingIntent? {
        val intent = Intent(context, StocksStonksWidget::class.java)
        intent.action = "StonkReload"
        intent.putExtra("widgetId", appWidgetId)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(intent != null && context != null && intent.action.equals("StonkReload")) {
            updateAppWidget(
                context, AppWidgetManager.getInstance(context), intent.getIntExtra(
                    "widgetId",
                    0
                )
            )
        }
    }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        this.context = context
        this.appWidgetManager = appWidgetManager

        if(!isInitialised(context, appWidgetId)) return //This function is also called when the user adds the widget for the first time. At that stage, the request is ignored

        val views = RemoteViews(context.packageName, R.layout.stocks_stonks_widget)
        views.setViewVisibility(R.id.widget_loading, View.VISIBLE)
        views.setOnClickPendingIntent(
            R.id.img_reload,
            getPendingSelfIntent(context, appWidgetId)
        );
        views.setTextViewText(R.id.stock_name, context.getString(R.string.loading_widget))
        appWidgetManager.updateAppWidget(appWidgetId, views)

        val tickerName = getTickerSymbol(context, appWidgetId)
        val networkManager = NetworkManager(context)
        networkManager.getJSONResponse(NetworkManager.getStockURL(tickerName), this)
    }
}