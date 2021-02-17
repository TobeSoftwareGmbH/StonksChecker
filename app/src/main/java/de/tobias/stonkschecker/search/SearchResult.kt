package de.tobias.stonkschecker.search

class SearchResult (val name: String, val ticker_symbol: String, val value: Double){

    companion object {
        fun fromValues(name: String, ticker_symbol: String, value: Double) : SearchResult {
            return SearchResult(removeEMTags(name), removeEMTags(ticker_symbol), value)
        }

        private fun removeEMTags(string: String) : String {
            return string.replace("<em>", "").replace("</em>", "")
        }
    }
}