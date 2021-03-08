package de.tobias.stonkschecker.widget

import android.app.ActivityOptions
import android.appwidget.AppWidgetManager
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import de.tobias.stonkschecker.R
import kotlin.random.Random


/**
 * The configuration screen for the [StocksStonksWidget] AppWidget.
 */
class StocksStonksWidgetConfigureActivity :  AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private val requestCode = Random.nextInt(5000, 50000)

    private var tickerName: String? = null
    private var tickerSymbol: String? = null
    private var stockName: String? = null
    private var updateInterval = 1

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

    private fun setAnimation() {
        val slide = Slide()
        slide.slideEdge = Gravity.START
        slide.duration = 400
        slide.interpolator = AccelerateDecelerateInterpolator()
        window.exitTransition = slide
        window.enterTransition = slide
    }

    override fun onStart() {
        super.onStart()

        findViewById<ConstraintLayout>(R.id.settings_container_stock).setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivityForResult(
                Intent(
                    this,
                    StockSearchActivity::class.java
                ), requestCode, options.toBundle()
            )
        }

        findViewById<ConstraintLayout>(R.id.settings_container_name).setOnClickListener {
            showNameInputDialog()
        }

        findViewById<ConstraintLayout>(R.id.settings_container_update_interval).setOnClickListener {
            showUpdateIntervalDialog()
        }


        if(resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) //When dark mode is enable by the user, the gray background of the widget preview is set to be darker
            findViewById<ConstraintLayout>(R.id.widget_preview).setBackgroundColor(
                Color.DKGRAY)

        findViewById<FloatingActionButton>(R.id.fab_done).setOnClickListener { submitWidgetData() }
    }

    private fun showNameInputDialog() {
        val inputFieldLayout = LayoutInflater.from(this).inflate(R.layout.material_input_dialog, null, false)
        val inputField = inputFieldLayout.findViewById<TextInputEditText>(R.id.stock_name_input)
        inputField.requestFocus()
        inputField.setText(stockName)

        MaterialAlertDialogBuilder(this)
            .setView(inputFieldLayout)
            .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, _ ->  dialogInterface.dismiss() })
            .setPositiveButton(getString(R.string.ok), DialogInterface.OnClickListener { dialogInterface, i ->
                val newName = inputField.text.toString()
                stockName = newName
                findViewById<TextView>(R.id.value_stock_name).text = newName
                findViewById<TextView>(R.id.stock_title_preview).text = newName + ":"
                dialogInterface.dismiss()})
            .show()
    }

    private fun showUpdateIntervalDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.title_update_interval))
            .setItems(resources.getTextArray(R.array.update_intervals), { dialogInterface, item ->  setUpdateInterval(item)})
            .show()
    }

    private fun setUpdateInterval(listItem: Int) {
        updateInterval = listItem
        findViewById<TextView>(R.id.value_update_interval).text = resources.getTextArray(R.array.update_intervals)[listItem]
    }

    private fun setStock(ticker_symbol: String, name: String) {
        this.tickerSymbol = ticker_symbol
        this.tickerName = name
        this.stockName = name

        findViewById<TextView>(R.id.value_stock_name).text = name
        findViewById<TextView>(R.id.value_tracked_stock).text = name
        findViewById<TextView>(R.id.stock_title_preview).text = name + ":"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == this.requestCode && resultCode == RESULT_OK && data != null) {
            setStock(data.getStringExtra("ticker")!!, data.getStringExtra("name")!!)
        }
    }

    private fun submitWidgetData() {
        if(stockName == null || tickerName == null || tickerSymbol == null) {
            //Values are not set, handle error
            Toast.makeText(this, getString(R.string.message_missing_entries), Toast.LENGTH_LONG).show()
            return
        } else {
            WidgetManager.saveStockData(
                this,
                appWidgetId,
                tickerSymbol!!,
                stockName!!,
                updateInterval
            )

            val interval = resources.getIntArray(R.array.update_intervals_ms)[updateInterval]
            if(interval != 0) StocksStonksWidget.setupPeriodicUpdate(this, interval, appWidgetId) //0 = do not update automatically

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

}
