package `in`.vikram.nytimes.data.remote

import androidx.annotation.VisibleForTesting

class AppApiHelper(private val apiService: ApiService) : ApiHelper, BaseDataSource() {

    companion object {
        private var INSTANCE: AppApiHelper? = null

        @JvmStatic
        fun getInstance(apiService: ApiService): AppApiHelper {
            if (INSTANCE == null) {
                synchronized(AppApiHelper::javaClass) {
                    INSTANCE =
                        AppApiHelper(apiService)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    override suspend fun getSearchResults(query : String, page : Int) = getResult { apiService.getSearchResults(query, page) }
}