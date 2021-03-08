package de.tobias.stonkschecker.widget

import android.app.ActivityOptions
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import de.tobias.stonkschecker.R


/**
 * The configuration screen for the [StocksStonksWidget] AppWidget.
 */
class StocksStonksWidgetConfigureActivity :  AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID


    public override fun onCreate(icicle: Bundle?) {
        setTheme(R.style.AppTheme_WidgetConfiguration)
        super.onCreate(icicle)

        setAnimation()

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

    fun setAnimation() {
        val slide = Slide()
        slide.slideEdge = Gravity.LEFT
        slide.duration = 400
        slide.interpolator = AccelerateDecelerateInterpolator()
        window.exitTransition = slide
        window.enterTransition = slide
    }

    override fun onStart() {
        super.onStart()

        findViewById<ConstraintLayout>(R.id.settings_container_stock).setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(
                Intent(
                    this,
                    StockSearchActivity::class.java
                ), options.toBundle()
            )
            Log.v("WidgetConfig", "Starting Stock Search Activity")
        }

    }

    fun saveData() {
        WidgetManager.saveStockData(
            this,
            appWidgetId,
            "",//searchResult.ticker_symbol,
            ""//searchResult.name
        )

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(this)
        StocksStonksWidget().updateAppWidget(this, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId in our intent
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}
