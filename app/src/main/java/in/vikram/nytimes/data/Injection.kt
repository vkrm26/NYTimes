package `in`.vikram.nytimes.data

import `in`.vikram.nytimes.data.remote.ApiService
import `in`.vikram.nytimes.data.remote.AppApiHelper
import android.content.Context
import kotlinx.coroutines.Dispatchers

object Injection {

    fun provideDataRepository(context: Context): DataSourceInterface {
        return DataRepository.getInstance(
            Dispatchers.IO,
            AppApiHelper.getInstance(ApiService.create()))

    }
}