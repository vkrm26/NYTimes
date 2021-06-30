package `in`.vikram.nytimes.custom

import `in`.vikram.nytimes.data.model.News

interface ItemClickListener {

     fun onNewsClick(news: News)

}