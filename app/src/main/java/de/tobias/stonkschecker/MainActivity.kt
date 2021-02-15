package de.tobias.stonkschecker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.tobias.stonkschecker.network.NetworkCallback
import de.tobias.stonkschecker.network.NetworkManager
import de.tobias.stonkschecker.search.SearchResults
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val networkManager = NetworkManager(this)
        networkManager.getJSONResponse(NetworkManager.getSearchURL("GameStop"), ValueReturn())
    }

    class ValueReturn: NetworkCallback {
        override fun onFinished(jsonArray: JSONArray) {
            SearchResults.parseSearchResponse(jsonArray).toString()

        }
    }
}