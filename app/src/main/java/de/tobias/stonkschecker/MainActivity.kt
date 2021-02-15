package de.tobias.stonkschecker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.tobias.stonkschecker.network.StocksManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stocksManager = StocksManager(this)
        stocksManager.getJSONResponse(stocksManager.getStockURL("GME:US"))
    }
}