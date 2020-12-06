package com.dixitpatel.pokemondemo.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.constant.PAGING_SIZE
import com.dixitpatel.pokemondemo.databinding.ActivityDetailViewBinding
import com.dixitpatel.pokemondemo.model.Pokemon
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.dixitpatel.pokemondemo.network.AuthStatus
import com.dixitpatel.pokemondemo.ui.base.BaseActivity
import com.dixitpatel.pokemondemo.utils.Alerts
import com.dixitpatel.pokemondemo.utils.Utils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject

/**
 *  Detail Activity : Detail View of Pokemon
 *  It will open after clicking on Pokemon Item
 */
class DetailViewActivity : BaseActivity<DetailViewModel>()
{
    private lateinit var binding: ActivityDetailViewBinding

    companion object
    {
         var SELECTION_TITLE = "selection_title"
         var SELECTION_IMAGE_URL = "selection_image_url"
        const val EXTRA_IMAGE_TRANSITION_NAME = "image_trans"
    }

    private lateinit var selectedPokemon : String
    private lateinit var selectedPokemonUrl : String

    @Inject
    lateinit var apiInterface : ApiInterface

    @Inject
    lateinit var models: DetailViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_view)
        supportPostponeEnterTransition()
        binding.viewModel = models
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true);

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_material);

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // get data from intent
        intent.let {
            selectedPokemon = intent.getStringExtra(SELECTION_TITLE) as String
            selectedPokemonUrl = intent.getStringExtra(SELECTION_IMAGE_URL) as String
            binding.toolbar.title = selectedPokemon
            supportActionBar?.title = selectedPokemon

            val imageTransitionName = intent.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME)
            binding.ivPokemonHeader.transitionName = imageTransitionName

            // Show Image With transition
             Picasso.get()
                    .load(selectedPokemonUrl)
                    .placeholder(R.drawable.icon_loading_place_holder)
                    .error(R.drawable.icon_loading_place_holder)
                    .into(binding.ivPokemonHeader, object : Callback {
                        override fun onSuccess() {
                            supportStartPostponedEnterTransition()
                        }
                        override fun onError(e: Exception?) {
                            supportStartPostponedEnterTransition()
                        }
                    })
            }

        binding.appBar.outlineProvider = null
        binding.appBar.elevation = 0F
        binding.toolbar.elevation = 0F
        binding.toolbarLayout.elevation = 0F
        binding.toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.color_primary))

        registerObserver()
        fetchPokemonDetail()
    }

    // Register Observer of fetching pokemon detail
    @SuppressLint("SetTextI18n")
    private fun registerObserver()
    {
        models.pokemonDetailApiResult().observe(
            this, androidx.lifecycle.Observer
            { result ->

                when (result.status) {
                    AuthStatus.LOADING -> {

                        Alerts.showProgressBar(this)
                    }
                    AuthStatus.ERROR -> {

                        Alerts.dismissProgressBar()
                        Alerts.showSnackBar(this@DetailViewActivity,result.message)
                    }
                    AuthStatus.SUCCESS -> {

                        Alerts.dismissProgressBar()


                        var types = ""
                        result.data?.types.let {
                            if (it != null) {
                                for (type in it) {
                                    types += type.type.name+", "
                                }
                            }
                        }
                        binding.tvPokemonTypes.text = "${getString(R.string.types)} $types"

                        var abilitiesString = ""
                        result.data?.abilities.let {
                            if (it != null) {
                                for (ability in it) {
                                    abilitiesString += ability.ability.name+", "
                                }
                            }
                        }
                        binding.tvPokemonAbilities.text = "${getString(R.string.abilities)} $abilitiesString"

                        binding.data = result.data
                        binding.executePendingBindings()
                    }
                }
            })
    }

    // fetching pokemon detail Api
    private fun fetchPokemonDetail()
    {
        if(Utils.isNetworkAvailable(this))
        {
            apiInterface.let { models.pokemonDetailApiCall(selectedPokemon, apiInterface) }
        } else {
            Alerts.showSnackBar(this, getString(R.string.internet_not_available))
        }
    }

    // Bind ViewModel
    override fun getViewModel(): DetailViewModel {
        return models
    }

}