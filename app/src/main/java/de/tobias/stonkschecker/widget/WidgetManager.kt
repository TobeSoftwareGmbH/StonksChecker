package de.tobias.stonkschecker.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context

class WidgetManager {
    companion object {
        private const val PREFS_NAME = "de.tobias.stonkschecker.StocksStonksWidget"
        private const val PREF_PREFIX_KEY = "appwidget_"

        //Retrieves all widget ids associated with this app
        fun getWidgetIds(context: Context) : IntArray {
            val name = ComponentName(context, StocksStonksWidget::class.java)
            return AppWidgetManager.getInstance(context).getAppWidgetIds(name)
        }

        //Retrieves all widget ids, but discards the ids of non-existing widgets
        fun getActiveWidgetIds(context: Context) : IntArray {
            val ids = getWidgetIds(context)
            val activeIds = ArrayList<Int>()
            for (id in ids) {
                if(isInitialised(context, id)) activeIds.add(id)
            }

            return activeIds.toIntArray()
        }

        fun getWidgetIdFromTickerSymbol(context: Context, ticker_symbol: String) : Int {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)

            //Get all widget ids
            val ids = getWidgetIds(context)

            for (i in ids.indices) {
                if(prefs.getString(PREF_PREFIX_KEY + ids[i], "").equals(ticker_symbol)) return ids[i]
            }

            throw IllegalStateException("No Widget ID found matching ticker symbol $ticker_symbol")
        }

        // Write the prefix to the SharedPreferences object for this widget
        fun saveStockData(context: Context, appWidgetId: Int, ticker_symbol: String, name: String) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putString(PREF_PREFIX_KEY + appWidgetId, ticker_symbol)
            prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_name", name)
            prefs.apply()
        }

        fun getTickerSymbol(context: Context, appWidgetId: Int): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getString(PREF_PREFIX_KEY + appWidgetId, "")!!
        }

        fun getStockName(context: Context, appWidgetId: Int): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getString(PREF_PREFIX_KEY + appWidgetId + "_name", "")!!
        }

        fun deleteStockData(context: Context, appWidgetId: Int) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.remove(PREF_PREFIX_KEY + appWidgetId)
            prefs.remove(PREF_PREFIX_KEY + appWidgetId + "_name")
            prefs.apply()
        }

        fun isInitialised(context: Context, appWidgetId: Int) : Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.contains(PREF_PREFIX_KEY + appWidgetId + "_name")
        }
    }
}