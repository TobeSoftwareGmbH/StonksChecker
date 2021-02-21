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

    override fun onStart() {
        super.onStart()

        //Get all widget ids
        val widgetIds = WidgetManager.getActiveWidgetIds(this)
        if (widgetIds.isEmpty()) { //No widgets added, display a message to the user
            findViewById<View>(R.id.container_no_widgets).visibility = View.VISIBLE
            findViewById<View>(R.id.container_widget_overview).visibility = View.GONE
        } else { //Widgets added, show an overview to the user
            findViewById<View>(R.id.container_no_widgets).visibility = View.GONE
            findViewById<View>(R.id.container_widget_overview).visibility = View.VISIBLE
        }
    }

}