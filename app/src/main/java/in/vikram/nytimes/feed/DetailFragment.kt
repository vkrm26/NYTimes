package `in`.vikram.nytimes.feed

import `in`.vikram.nytimes.BuildConfig
import `in`.vikram.nytimes.data.model.News
import `in`.vikram.nytimes.databinding.FragmentDetailBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide

class DetailFragment : Fragment() {

    private var binding : FragmentDetailBinding?= null
    private var news : News ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater)

        news = arguments?.getParcelable<News>("news")

        val navController = findNavController()
        binding?.toolbar?.setupWithNavController(navController)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.let {

            news?.multimedia?.let {

                if (it.isNotEmpty()) {
                    Glide.with(binding!!.imgFullSize)
                        .load(BuildConfig.IMAGE_URL +  news?.multimedia?.get(0)?.url)
                        .into(binding!!.imgFullSize)
                }
            }

            if (!news?.headline?.main.isNullOrBlank()) {
                binding!!.txtTitle.text = news?.headline?.main
            } else if (!news?.headline?.print_headline.isNullOrBlank()) {
                binding!!.txtTitle.text = news?.headline?.print_headline
            }

            binding!!.txtSubTitle.text = news?.snippet
            binding!!.txtTime.text = news?.pubDate

        }


    }

}