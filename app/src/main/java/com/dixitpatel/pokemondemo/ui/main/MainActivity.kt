package com.dixitpatel.pokemondemo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.constant.PAGING_SIZE
import com.dixitpatel.pokemondemo.databinding.ActivityMainBinding
import com.dixitpatel.pokemondemo.databinding.ItemLoadMoreBinding
import com.dixitpatel.pokemondemo.databinding.RowItemAllBinding
import com.dixitpatel.pokemondemo.model.Pokemon
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.dixitpatel.pokemondemo.network.AuthStatus
import com.dixitpatel.pokemondemo.ui.base.BaseActivity
import com.dixitpatel.pokemondemo.ui.detail.DetailViewActivity
import com.dixitpatel.pokemondemo.utils.Alerts
import com.dixitpatel.pokemondemo.utils.CommonAdapter
import com.dixitpatel.pokemondemo.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityViewModel?>(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var model: MainActivityViewModel

    @Inject
    lateinit var apiInterface : ApiInterface

    override fun getViewModel(): MainActivityViewModel {
        return model
    }

    private var backPressedTime: Long = 0

    private var offset = 0
    private var isNextPage = false
    private var isLoadMore = false
    private var isRefresh = false

    lateinit var mAdapter: CommonAdapter<Pokemon>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.title = getString(R.string.app_name)

        binding.lifecycleOwner = this

        binding.progressBar.backgroundProgressBarColor = ContextCompat.getColor(this, R.color.transparent)
        binding.progressBar.backgroundProgressBarWidth = 4F
        binding.progressBar.progressBarWidth = 4F
        binding.progressBar.progressBarColor = ContextCompat.getColor(this, R.color.color_primary)
        binding.progressBar.indeterminateMode = true
        binding.progressBar.roundBorder = true

        registerObserver()
        fetchPokemonList()

    }

    private fun registerObserver()
    {
        model.pokemonApiResult().observe(
            this, androidx.lifecycle.Observer
            { result ->

                when (result.status) {
                    AuthStatus.LOADING -> {

                        if(!isRefresh && !isLoadMore)
                            binding.progressBar.visibility = View.VISIBLE
                        binding.tvNoResultTxt.visibility = View.GONE
                    }
                    AuthStatus.ERROR -> {

                        isRefresh = false
                        binding.progressBar.visibility = View.GONE
                        Alerts.showSnackBar(this, result.message)

                    }
                    AuthStatus.SUCCESS -> {

                        isRefresh = false
                        binding.progressBar.visibility = View.GONE

                        if (result.data?.results?.isNotEmpty()!!) {

                            if (result.data.next != null) {
                                isNextPage = true
                                offset += PAGING_SIZE
                            } else {
                                isNextPage = false
                            }

                            Timber.e("Timber : offset $offset")

                            val arrData: ArrayList<Pokemon?> = result.data.results

                            if (isLoadMore) {
                                mAdapter.stopLoadMore(arrData)
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
                            mAdapter.setMoreDataAvailable(isNextPage)

                        } else {
                            binding.tvNoResultTxt.visibility = View.VISIBLE
                            binding.myRecyclerView.visibility = View.GONE
                        }
                    }
                }
            })

        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun fetchPokemonList()
    {
        if(Utils.isNetworkAvailable(this))
        {
            apiInterface.let { model.pokemonApiCall(offset, apiInterface) }
        } else {
            Alerts.showSnackBar(this, getString(R.string.internet_not_available))
        }
    }



    override fun onBackPressed() {
        if (backPressedTime + 2500 > System.currentTimeMillis()) {
            super.onBackPressed()
            ActivityCompat.finishAffinity(this)
        } else {
            Alerts.showSnackBar(
                this@MainActivity, resources.getString(R.string.double_press_exit))
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
            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRefresh() {
        try {
            offset = 0
            isLoadMore = false
            isRefresh = true
            mAdapter.getData().clear()
            mAdapter.notifyDataSetChanged()

            fetchPokemonList()

            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onItemsLoadComplete() {
        isRefresh = false
        binding.swipeRefreshLayout.isRefreshing = false
    }


    private fun setAdapter(arrData: ArrayList<Pokemon?>) {
        mAdapter = object : CommonAdapter<Pokemon>(arrData) {


            override fun getItemViewType(position: Int): Int {
                return if (getItem(position) == null) R.layout.item_load_more else R.layout.row_item_all
            }

            override fun onBind(holder: CommonViewHolder?, position: Int) {
                try {
                    if (holder?.binding is RowItemAllBinding)
                    {
                        val binding: RowItemAllBinding = holder.binding as RowItemAllBinding

                        binding.tvPokemonName.text = arrData[position]?.name

                        Picasso.get()
                            .load(arrData[position]?.getImageUrl())
                            .placeholder(R.drawable.icon_loading_place_holder)
                            .error(R.drawable.icon_loading_place_holder)
                            .into(binding.ivPokemonImage)

                        binding.root.setOnClickListener {

                            val intent = Intent(this@MainActivity, DetailViewActivity::class.java)

                            ViewCompat.setTransitionName((holder.binding as RowItemAllBinding).ivPokemonImage, arrData[position]?.name)

                           intent.putExtra(DetailViewActivity.EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName((holder.binding as RowItemAllBinding).ivPokemonImage))

                            val options: ActivityOptionsCompat =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    this@MainActivity,
                                    (holder.binding as RowItemAllBinding).ivPokemonImage,
                                    ViewCompat.getTransitionName((holder.binding as RowItemAllBinding).ivPokemonImage)!!
                                )

                            intent.putExtra(DetailViewActivity.SELECTION_TITLE  , arrData[position]?.name)
                            intent.putExtra(DetailViewActivity.SELECTION_IMAGE_URL  , arrData[position]?.getImageUrl())
                            startActivity(intent,options.toBundle())


                        }
                    } else {
                        val binding: ItemLoadMoreBinding = holder?.binding as ItemLoadMoreBinding
                        binding.progressBar.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.myRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false)
        binding.myRecyclerView.adapter = mAdapter

        mAdapter.setLoadMoreListener(object : CommonAdapter.OnLoadMoreListener{
            override fun onLoadMore() {
                binding.myRecyclerView.post {
                    mAdapter.startLoadMore()
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