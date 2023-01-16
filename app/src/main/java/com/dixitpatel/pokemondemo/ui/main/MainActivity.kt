package com.dixitpatel.pokemondemo.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.constant.PAGING_SIZE
import com.dixitpatel.pokemondemo.databinding.ActivityMainBinding
import com.dixitpatel.pokemondemo.databinding.ItemLoadMoreBinding
import com.dixitpatel.pokemondemo.databinding.RowItemAllBinding
import com.dixitpatel.pokemondemo.extension.recyclerViewAnimate
import com.dixitpatel.pokemondemo.model.Pokemon
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.dixitpatel.pokemondemo.network.AuthStatus
import com.dixitpatel.pokemondemo.pref.PrefEntity
import com.dixitpatel.pokemondemo.pref.Preferences
import com.dixitpatel.pokemondemo.ui.base.BaseActivity
import com.dixitpatel.pokemondemo.ui.detail.DetailViewActivity
import com.dixitpatel.pokemondemo.utils.Alerts
import com.dixitpatel.pokemondemo.utils.CommonAdapter
import com.dixitpatel.pokemondemo.utils.Utils
import com.dixitpatel.pokemondemo.utils.Utils.Companion.loadSvg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**
 *  Main Activity : Pokemon Listing Activity
 */
class MainActivity : BaseActivity<MainActivityViewModel?>(), SwipeRefreshLayout.OnRefreshListener {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    @Inject
    lateinit var model: MainActivityViewModel

    @Inject
    lateinit var apiInterface: ApiInterface

    override fun getViewModel(): MainActivityViewModel {
        return model
    }

    private var backPressedTime: Long = 0

    private var offset = 0
    private var isNextPage = false
    private var isLoadMore = false
    private var isRefresh = false

    var mAdapter: CommonAdapter<Pokemon>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbar.run {
            setSupportActionBar(this)
            this.title = getString(R.string.app_name)
            this.setNavigationOnClickListener {
                onManualBackPressed()
            }
        }

        binding.viewModel = model
        binding.lifecycleOwner = this

        binding.progressBar.run {
            roundBorder = true
            indeterminateMode = true
            progressBarColor = ContextCompat.getColor(this@MainActivity, R.color.color_primary)
            progressBarWidth = 4F
            backgroundProgressBarWidth = 4F
            backgroundProgressBarColor =
                ContextCompat.getColor(this@MainActivity, R.color.transparent)
        }

        registerObserver()
        fetchPokemonList()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onManualBackPressed()
            }

        })
    }


    // Register Listing observer
    private fun registerObserver() {
        model.mainViewRepository.pokemonApiResult().observe(this) { result ->
            when (result.status) {
                AuthStatus.LOADING -> {

                    if (!isLoadMore) {
                        binding.shimmerLayout.showShimmer(true)
                        binding.shimmerLayout.visibility = View.VISIBLE
                    }
                    binding.tvNoResultTxt.visibility = View.GONE
                }
                AuthStatus.ERROR -> {

                    isRefresh = false
                    Alerts.showSnackBar(this, result.message)
                    binding.shimmerLayout.hideShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                }
                AuthStatus.SUCCESS -> {

                    isRefresh = false

                    binding.shimmerLayout.hideShimmer()
                    binding.shimmerLayout.visibility = View.GONE

                    if (!result.data?.results.isNullOrEmpty()) {

                        if (!result.data?.next.isNullOrEmpty()) {
                            isNextPage = true
                            offset += PAGING_SIZE
                        } else {
                            isNextPage = false
                        }

                        Timber.e("Timber : offset $offset")

                        val arrData: ArrayList<Pokemon?> = result.data!!.results

                        if (isLoadMore) {
                            mAdapter?.stopLoadMore(arrData)
                        } else {
                            if (isRefresh) {
                                if (arrData.size > 0) {
                                    binding.tvNoResultTxt.visibility = View.INVISIBLE
                                    binding.myRecyclerView.visibility = View.VISIBLE
                                    setAdapter(arrData)
                                }
                                onItemsLoadComplete()
                            } else {
                                if (arrData.size > 0) {
                                    binding.myRecyclerView.visibility = View.VISIBLE
                                    setAdapter(arrData)
                                } else {
                                    if (isRefresh) {
                                        onItemsLoadComplete()
                                    }
                                    binding.myRecyclerView.visibility = View.GONE
                                    binding.tvNoResultTxt.visibility = View.VISIBLE
                                }
                            }
                        }
                        mAdapter?.setMoreDataAvailable(isNextPage)

                    } else {
                        binding.tvNoResultTxt.visibility = View.VISIBLE
                        binding.myRecyclerView.visibility = View.GONE
                    }
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    // fetch Pokemon Listing data
    private fun fetchPokemonList() {
        if (Utils.isNetworkAvailable(this)) {
            model.mainViewRepository.pokemonApiCall(offset, apiInterface)
        } else {
            Alerts.showSnackBar(this, getString(R.string.internet_not_available))
        }
    }

    fun onManualBackPressed() {
        if (backPressedTime + 2500 > System.currentTimeMillis()) {
            this@MainActivity.finish()
            ActivityCompat.finishAffinity(this)
        } else {
            Alerts.showSnackBar(
                this@MainActivity, resources.getString(R.string.double_press_exit)
            )
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_list_grid -> {

                if (Preferences.getPreferenceInt(PrefEntity.IS_LIST_VIEW) == 0) {

                    binding.myRecyclerView.layoutManager = GridLayoutManager(
                        this, 2, RecyclerView.VERTICAL, false
                    )
                    item.setIcon(R.drawable.ic_view_list_24)
                    Preferences.setPreferenceInt(PrefEntity.IS_LIST_VIEW, 1)
                    binding.myRecyclerView.recyclerViewAnimate()
                } else {
                    binding.myRecyclerView.layoutManager = LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false
                    )
                    item.setIcon(R.drawable.ic_grid_on_24)
                    binding.myRecyclerView.recyclerViewAnimate()
                    Preferences.setPreferenceInt(PrefEntity.IS_LIST_VIEW, 0)
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // onRefresh call when swipe to refresh called.
    @SuppressLint("NotifyDataSetChanged")
    override fun onRefresh() {
        try {
            offset = 0
            isLoadMore = false
            isRefresh = true
            mAdapter?.let {
                it.getData().clear()
                it.notifyDataSetChanged()
            }

            fetchPokemonList()

            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // item load completed
    private fun onItemsLoadComplete() {
        isRefresh = false
        binding.swipeRefreshLayout.isRefreshing = false
    }


    // set Adapter
    private fun setAdapter(arrData: ArrayList<Pokemon?>) {
        mAdapter = object : CommonAdapter<Pokemon>(arrData) {

            override fun getItemViewType(position: Int): Int {
                return if (getItem(position) == null) R.layout.item_load_more else R.layout.row_item_all
            }

            override fun onBindWithData(holder: CommonViewHolder, position: Int, item: Pokemon?) {

                try {
                    if (holder.binding is RowItemAllBinding) {
                        val binding: RowItemAllBinding = holder.binding as RowItemAllBinding

                        binding.tvPokemonName.text = item?.name
                        binding.tvPokemonName.visibility = View.VISIBLE

                        item?.getImageUrl()?.let {
                            binding.ivPokemonImage.loadSvg(it)
                        }

                        binding.root.setOnClickListener {

                            // Detail Activity will open when click on pokemon Item
                            val intent = Intent(this@MainActivity, DetailViewActivity::class.java)

                            ViewCompat.setTransitionName(
                                (holder.binding as RowItemAllBinding).ivPokemonImage,
                                item?.url
                            )

                            intent.putExtra(
                                DetailViewActivity.EXTRA_IMAGE_TRANSITION_NAME,
                                ViewCompat.getTransitionName(
                                    (holder.binding as RowItemAllBinding).ivPokemonImage
                                )
                            )
                            val pair1 = Pair.create<View, String>(
                                (holder.binding as RowItemAllBinding).ivPokemonImage,
                                ViewCompat.getTransitionName(
                                    (holder.binding as RowItemAllBinding).ivPokemonImage
                                )
                            )
                            val options: ActivityOptionsCompat =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    this@MainActivity,
                                    pair1
                                )
                            intent.putExtra(
                                DetailViewActivity.SELECTION_TITLE,
                                item?.name
                            )
                            intent.putExtra(
                                DetailViewActivity.SELECTION_IMAGE_URL,
                                item?.getImageUrl()
                            )
                            startActivity(intent, options.toBundle())

                        }
                    } else {
                        val binding: ItemLoadMoreBinding = holder.binding as ItemLoadMoreBinding
                        binding.progressBar.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (Preferences.getPreferenceInt(PrefEntity.IS_LIST_VIEW) == 0) {

            binding.myRecyclerView.layoutManager = LinearLayoutManager(
                this, RecyclerView.VERTICAL, false
            )
        } else {
            binding.myRecyclerView.layoutManager = GridLayoutManager(
                this, 2, RecyclerView.VERTICAL, false
            )
        }

        binding.myRecyclerView.adapter = mAdapter
        binding.myRecyclerView.recyclerViewAnimate()

        // Load more listener will fetch another data.
        mAdapter?.setLoadMoreListener(object : CommonAdapter.OnLoadMoreListener {
            override fun onLoadMore() {
                binding.myRecyclerView.post {
                    mAdapter?.startLoadMore()
                    CoroutineScope(Dispatchers.Main).launch {
                        isLoadMore = true
                        delay(500)
                        fetchPokemonList()
                    }
                }
            }

        })
    }

}