package de.tobias.stonkschecker.search

class SearchResult (val name: String, val ticker_symbol: String){

    companion object {
        fun fromValues(name: String, ticker_symbol: String) : SearchResult {
            return SearchResult(removeEMTags(name), removeEMTags(ticker_symbol))
        }

        fun removeEMTags(string: String) : String {
            return string.replace("<em>", "").replace("</em>", "")
        }
    }
}