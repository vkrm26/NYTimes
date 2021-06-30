package `in`.vikram.nytimes.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class News() : Parcelable {


    @SerializedName("web_url")
    @Expose
    var webUrl: String? = null


    @SerializedName("source")
    @Expose
    var source: String? = null

    @SerializedName("snippet")
    @Expose
    var snippet: String? = null

    @SerializedName("multimedia")
    @Expose
    var multimedia: List<MultiMedia>? = null

    @SerializedName("headline")
    @Expose
    var headline: HeadLine? = null

    @SerializedName("pub_date")
    @Expose
    var pubDate: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("word_count")
    @Expose
    var wordCount: Int? = null


    constructor(parcel: Parcel) : this() {
        webUrl = parcel.readString()
        source = parcel.readString()
        pubDate = parcel.readString()
        id = parcel.readString()
        snippet = parcel.readString()
        wordCount = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(webUrl)
        parcel.writeString(source)
        parcel.writeString(pubDate)
        parcel.writeString(id)
        parcel.writeValue(snippet)
        parcel.writeValue(wordCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<News> {

        override fun createFromParcel(parcel: Parcel): News {
            return News(parcel)
        }

        override fun newArray(size: Int): Array<News?> {
            return arrayOfNulls(size)
        }
    }
}