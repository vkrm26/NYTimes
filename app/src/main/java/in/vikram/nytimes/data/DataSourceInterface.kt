package `in`.vikram.nytimes.data

import `in`.vikram.nytimes.data.model.News
import `in`.vikram.nytimes.custom.Resource

interface DataSourceInterface {

    suspend fun getSearchResults(query : String, page : Int) : Resource<MutableList<News>?>

}