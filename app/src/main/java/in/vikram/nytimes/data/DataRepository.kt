package `in`.vikram.nytimes.data

import `in`.vikram.nytimes.const.Const.Error.NO_INTERNET
import `in`.vikram.nytimes.const.Const.Error.TRY_AGAIN
import `in`.vikram.nytimes.const.Const.Error.UNABLE_TO_RESOLVE_HOST
import `in`.vikram.nytimes.custom.Resource
import `in`.vikram.nytimes.custom.Status.SUCCESS
import `in`.vikram.nytimes.data.model.News
import `in`.vikram.nytimes.data.remote.ApiHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default, private var apiHelper: ApiHelper) :
    DataSourceInterface {


    companion object {

        private var INSTANCE: DataRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance(
            defaultDispatcher: CoroutineDispatcher,
            apiHelper: ApiHelper
        ): DataRepository {
            return INSTANCE ?: DataRepository(defaultDispatcher, apiHelper)
                .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }




    override suspend fun getSearchResults(query: String, page: Int) : Resource<MutableList<News>?> =
        withContext(defaultDispatcher) {
            val results = apiHelper.getSearchResults(query, page)
            if (results.status == SUCCESS) {
                if (results.data?.response?.docs != null) {
                    Resource.success(results.data?.response?.docs!!)
                }
                else {
                    Resource.error(TRY_AGAIN)
                }

            } else {
                if (results.message != null) {
                    if (results.message.contains(UNABLE_TO_RESOLVE_HOST, true)) {
                        Resource.error(NO_INTERNET)
                    } else {
                        Resource.error(TRY_AGAIN)
                    }
                } else {
                    Resource.error(TRY_AGAIN)
                }

            }
        }


}