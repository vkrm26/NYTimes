package `in`.vikram.nytimes.data.remote.responses

import `in`.vikram.nytimes.data.model.News
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetNewsResults {

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("response")
    @Expose
    var response: NewsResponse? = null


    class NewsResponse {

        @SerializedName("docs")
        @Expose
        var docs: MutableList<News>? = null
    }
}

