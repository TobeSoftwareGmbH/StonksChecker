package de.tobias.stonkschecker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.tobias.stonkschecker.network.NetworkManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stocksManager = NetworkManager(this)
        stocksManager.getJSONResponse(stocksManager.getStockURL("GME:US"))
    }
}