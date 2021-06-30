
import `in`.vikram.nytimes.data.model.News
import `in`.vikram.nytimes.databinding.ItemNewsBinding
import `in`.vikram.nytimes.databinding.LayoutProgressBinding
import `in`.vikram.nytimes.viewholders.BaseVH
import `in`.vikram.nytimes.viewholders.NewsVH
import `in`.vikram.nytimes.viewholders.ProgressVH
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(private val mNewsList: MutableList<News?>, private val onNewsItemClicked: (News) -> Unit) : RecyclerView.Adapter<BaseVH>() {


    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        if (viewType == VIEW_TYPE_ITEM) {
            val albumBinding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context))
            return NewsVH(albumBinding)
        } else {
            val layoutProgressBinding = LayoutProgressBinding.inflate(LayoutInflater.from(parent.context))
            return ProgressVH(layoutProgressBinding)
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {

        if (holder is NewsVH) {
            mNewsList[position]?.let { holder.bind(it) }
            holder.itemView.setOnClickListener {
                mNewsList[holder.adapterPosition]?.let { it1 -> onNewsItemClicked(it1) }
            }
        }
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (mNewsList[position] != null)
            return VIEW_TYPE_ITEM;
        else
            return VIEW_TYPE_LOADING;
    }

    fun clearItems() {
        mNewsList.clear()
    }

    fun addItems(newsList: List<News>) {
        val newsDiffUtil = DiffUtil.calculateDiff(NewsDiffUtil(this.mNewsList, newsList))
        this.mNewsList.clear()
        this.mNewsList.addAll(newsList)
        this.mNewsList.add(null)
        newsDiffUtil.dispatchUpdatesTo(this)
    }

//    fun addNullData() {
//        this.mNewsList.add(null)
//        notifyItemInserted(this.mNewsList.size - 1)
//    }
//
//    fun removeNull() {
//        if (this.mNewsList.isNotEmpty() )
//            this.mNewsList.removeAt(this.mNewsList.size - 1)
//        notifyItemRemoved(this.mNewsList.size)
//    }

    class NewsDiffUtil(private val oldList: MutableList<News?>, private val newList: List<News>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition]?.id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        @Nullable
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
            //you can return particular field for changed item.
            super.getChangePayload(oldItemPosition, newItemPosition)

    }


}