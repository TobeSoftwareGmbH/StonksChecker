package de.tobias.stonkschecker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import de.tobias.stonkschecker.widget.WidgetManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        //Get all widget ids
        val widgetIds = WidgetManager.getActiveWidgetIds(this)
        if (widgetIds.isEmpty()) { //No widgets added, display a message to the user
            toggleNoWidgetsMessage(true)
        } else { //Widgets added, show an overview to the user
            toggleNoWidgetsMessage(false)
        }
    }

    private fun toggleNoWidgetsMessage (show: Boolean) {
        findViewById<View>(R.id.container_no_widgets).visibility = if(show) View.VISIBLE else View.GONE
        findViewById<View>(R.id.container_widget_overview).visibility = if(show) View.GONE else View.VISIBLE
    }

}