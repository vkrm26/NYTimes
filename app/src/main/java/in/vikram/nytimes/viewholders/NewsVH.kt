package `in`.vikram.nytimes.viewholders

import `in`.vikram.nytimes.BuildConfig
import `in`.vikram.nytimes.R
import `in`.vikram.nytimes.data.model.News
import `in`.vikram.nytimes.databinding.ItemNewsBinding
import com.bumptech.glide.Glide

class NewsVH(private val itemBinding: ItemNewsBinding) : BaseVH(itemBinding.root) {

    fun bind(news: News) {

        news?.multimedia?.let {

            if (it.isNotEmpty()) {
                Glide.with(itemBinding.imgPhoto)
                    .load(BuildConfig.IMAGE_URL + news.multimedia!![0].url)
                    .into(itemBinding.imgPhoto)
            } else {
                itemBinding.imgPhoto.setImageResource(R.drawable.ic_default)
            }
        }

        if (!news.headline?.main.isNullOrBlank()) {
            itemBinding.txtTitle.text = news.headline?.main
        } else if (!news.headline?.print_headline.isNullOrBlank()) {
            itemBinding.txtTitle.text = news.headline?.print_headline
        }
    }

}