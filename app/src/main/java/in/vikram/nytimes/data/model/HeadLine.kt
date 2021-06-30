package `in`.vikram.nytimes.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class HeadLine() : Parcelable {

    @SerializedName("main")
    @Expose
    var main: String? = null

    @SerializedName("print_headline")
    @Expose
    var print_headline: String? = null

    constructor(parcel: Parcel) : this() {
        main = parcel.readString()
        print_headline = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(main)
        parcel.writeString(print_headline)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<HeadLine> {

        override fun createFromParcel(parcel: Parcel): HeadLine {
            return HeadLine(parcel)
        }

        override fun newArray(size: Int): Array<HeadLine?> {
            return arrayOfNulls(size)
        }
    }
}