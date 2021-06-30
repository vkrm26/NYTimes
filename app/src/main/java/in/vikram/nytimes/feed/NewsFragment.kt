package `in`.vikram.nytimes.feed

import NewsAdapter
import `in`.vikram.nytimes.R
import `in`.vikram.nytimes.const.Const.Error.NO_CACHED_DATA
import `in`.vikram.nytimes.const.Const.Error.NO_INTERNET
import `in`.vikram.nytimes.custom.InfiniteScrollListener
import `in`.vikram.nytimes.custom.SpaceItemDecoration
import `in`.vikram.nytimes.custom.Status.DUMMY
import `in`.vikram.nytimes.custom.Status.ERROR
import `in`.vikram.nytimes.custom.Status.LOADING
import `in`.vikram.nytimes.custom.Status.SUCCESS
import `in`.vikram.nytimes.databinding.FragmentNewsBinding
import `in`.vikram.nytimes.feed.NewsFragment.VIEW_TYPE.GRID
import `in`.vikram.nytimes.feed.NewsFragment.VIEW_TYPE.LIST
import `in`.vikram.nytimes.viewmodel.NewsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

class NewsFragment : Fragment() {

    private var binding : FragmentNewsBinding?= null
    private var viewModel : NewsViewModel ?= null
    private var scrollListener : InfiniteScrollListener?= null
    private var gridLayoutManager : GridLayoutManager? = null
    private var currentViewType = GRID


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(layoutInflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        // layout Manager
        gridLayoutManager = GridLayoutManager(this.context, 2)
        binding?.recyclerView?.layoutManager = gridLayoutManager

        // adapter
        val newsAdapter = NewsAdapter(
            mutableListOf()
        ) { news ->
            val bundle = bundleOf("news" to news)
            findNavController().navigate(R.id.action_news_fragment_to_detail_fragment, bundle)
        }

        binding?.recyclerView?.adapter = newsAdapter

        binding?.toolbar?.title = "News"
        binding?.toolbar?.inflateMenu(R.menu.activity_menu)

        binding?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.change_layout -> {
                    if (currentViewType == GRID) currentViewType = LIST
                    else currentViewType = GRID
                    changeLayoutManager()

                    true
                }
                else -> false
            }
        }

        // item decoration in recycler
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dimen_space)
        binding?.recyclerView?.addItemDecoration(SpaceItemDecoration(spacingInPixels))

        // for infinite scroll in recycler
        scrollListener = InfiniteScrollListener(
            {
                viewModel?.getNextPage()
                scrollListener?.setLoadingFlag(true)

            }, gridLayoutManager!!)
        binding?.recyclerView?.addOnScrollListener(scrollListener!!)


        viewModel?.news?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> {
                    newsAdapter.clearItems()
                    newsAdapter.notifyDataSetChanged()
                    scrollListener?.reset()
                    binding?.placeholder?.visibility = View.VISIBLE
                    binding?.placeholder?.setAnimation(R.raw.searching_data)
                    binding?.placeholder?.playAnimation()
                    binding?.recyclerView?.visibility = View.GONE
                }
                ERROR -> {
                    binding?.recyclerView?.visibility = View.GONE
                    binding?.placeholder?.visibility = View.VISIBLE
//                    newsAdapter.removeNull()

                    when (it.message) {
                        NO_CACHED_DATA -> {
                            return@Observer
                        }
                        NO_INTERNET -> {
                            binding?.placeholder?.setAnimation(R.raw.network_error)
                        }
                        else -> {
                            binding?.placeholder?.setAnimation(R.raw.sad_search)
                        }
                    }

                    binding?.placeholder?.playAnimation()
                    scrollListener?.setLoadingFlag(false)
                }
                SUCCESS -> {
//                    newsAdapter.removeNull()
                    it?.data?.let { it1 -> newsAdapter.addItems(it1) }
                    binding?.placeholder?.visibility = View.GONE
                    binding?.recyclerView?.visibility = View.VISIBLE
                    scrollListener?.setLoadingFlag(false)
                }
                DUMMY -> {
//                    newsAdapter.addNullData()
                }
            }
        })



        viewModel?.onSearchQueryChanged("india")


    }

    private fun changeLayoutManager() {
        gridLayoutManager?.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (currentViewType == GRID) 1 else 2
            }
        }
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter?.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
        scrollListener?.let { binding?.recyclerView?.removeOnScrollListener(it) }
    }

    enum class VIEW_TYPE {
        GRID, LIST
    }

}