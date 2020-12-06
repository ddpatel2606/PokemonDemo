package com.dixitpatel.pokemondemo.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.databinding.ActivityDetailViewBinding
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.dixitpatel.pokemondemo.ui.base.BaseActivity
import com.dixitpatel.pokemondemo.utils.Utils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true);

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_material);

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        intent.let {
            selectedPokemon = intent.getStringExtra(SELECTION_TITLE) as String
            selectedPokemonUrl = intent.getStringExtra(SELECTION_IMAGE_URL) as String
            binding.toolbar.title = selectedPokemon

            val imageTransitionName = intent.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME)
            binding.ivPokemonHeader.transitionName = imageTransitionName

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

    }

    override fun getViewModel(): DetailViewModel {
        return models
    }

}