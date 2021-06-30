package `in`.vikram.nytimes.data.remote

import `in`.vikram.nytimes.custom.Resource
import `in`.vikram.nytimes.data.remote.responses.GetNewsResults

interface ApiHelper {

    suspend fun getSearchResults(query : String, page : Int): Resource<GetNewsResults>

}