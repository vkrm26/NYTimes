package `in`.vikram.nytimes.custom

import `in`.vikram.nytimes.custom.Status.DUMMY
import `in`.vikram.nytimes.custom.Status.ERROR
import `in`.vikram.nytimes.custom.Status.LOADING
import `in`.vikram.nytimes.custom.Status.SUCCESS

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T? = null): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }

        fun <T> empty(data: T? = null) : Resource<T> {
            return Resource(DUMMY, data, null)
        }
    }



    override fun toString(): String {
        return "Resource { " +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }

}
