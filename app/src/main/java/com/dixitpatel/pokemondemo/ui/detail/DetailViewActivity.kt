package com.dixitpatel.pokemondemo.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.databinding.ActivityDetailViewBinding
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.dixitpatel.pokemondemo.network.AuthStatus
import com.dixitpatel.pokemondemo.ui.base.BaseActivity
import com.dixitpatel.pokemondemo.utils.Alerts
import com.dixitpatel.pokemondemo.utils.Utils
import com.dixitpatel.pokemondemo.utils.Utils.Companion.loadSvg
import javax.inject.Inject

/**
 *  Detail Activity : Detail View of Pokemon
 *  It will open after clicking on Pokemon Item
 */
class DetailViewActivity : BaseActivity<DetailViewModel>() {
    private val binding: ActivityDetailViewBinding by binding(R.layout.activity_detail_view)

    companion object {
        const val SELECTION_TITLE = "selection_title"
        const val SELECTION_IMAGE_URL = "selection_image_url"
        const val EXTRA_IMAGE_TRANSITION_NAME = "image_trans"
    }

    private lateinit var selectedPokemon: String
    private lateinit var selectedPokemonUrl: String

    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var models: DetailViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        binding.viewModel = models

        binding.toolbar.run {
            setSupportActionBar(this)
            this.setNavigationOnClickListener { finish() }
            this.elevation = 0F
            supportActionBar?.let { its ->
                its.setDisplayHomeAsUpEnabled(true)
                its.setHomeButtonEnabled(true)
                its.setHomeAsUpIndicator(R.drawable.ic_back_material)
            }
        }

        // get data from intent
        intent?.let {
            selectedPokemon = it.getStringExtra(SELECTION_TITLE) as String
            selectedPokemonUrl = it.getStringExtra(SELECTION_IMAGE_URL) as String
            binding.toolbar.title = selectedPokemon
            supportActionBar?.title = selectedPokemon
            binding.ivPokemonHeader.transitionName =
                it.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME)

            // Show Image With transition
            binding.ivPokemonHeader.loadSvg(selectedPokemonUrl)

            supportStartPostponedEnterTransition()
        }

        binding.appBar.run {
            outlineProvider = null
            elevation = 0F
        }
        binding.toolbarLayout.run {
            elevation = 0F
            setCollapsedTitleTextColor(
                ContextCompat.getColor(
                    this@DetailViewActivity,
                    R.color.color_primary
                )
            )
        }

        registerObserver()
        fetchPokemonDetail()
    }

    // Register Observer of fetching pokemon detail
    @SuppressLint("SetTextI18n")
    private fun registerObserver() {
        models.detailViewRepository.pokemonDetailApiResult()
            .observe(this@DetailViewActivity) { result ->

                when (result.status) {
                    AuthStatus.LOADING -> {

                        Alerts.showProgressBar(this)
                    }
                    AuthStatus.ERROR -> {

                        Alerts.dismissProgressBar()
                        Alerts.showSnackBar(this@DetailViewActivity, result.message)
                    }
                    AuthStatus.SUCCESS -> {

                        Alerts.dismissProgressBar()

                        binding.tvPokemonTypes.text =
                            "${getString(R.string.types)} ${result.data?.types?.joinToString { it -> it.type.name }}"

                        binding.tvPokemonAbilities.text =
                            "${getString(R.string.abilities)} ${result.data?.abilities?.joinToString { it -> it.ability.name }}"

                        binding.data = result.data
                        binding.executePendingBindings()
                    }
                }
            }
    }

    // fetching pokemon detail Api
    private fun fetchPokemonDetail() {
        if (Utils.isNetworkAvailable(this)) {
            apiInterface.let {
                models.detailViewRepository.pokemonDetailApiCall(
                    selectedPokemon,
                    apiInterface
                )
            }
        } else {
            Alerts.showSnackBar(this, getString(R.string.internet_not_available))
        }
    }

    // Bind ViewModel
    override fun getViewModel(): DetailViewModel {
        return models
    }

}

