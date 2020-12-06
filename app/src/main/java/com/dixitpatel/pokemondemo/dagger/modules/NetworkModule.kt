package com.dixitpatel.pokemondemo.dagger.modules

import android.content.Context
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.constant.BASE_URL
import com.dixitpatel.pokemondemo.constant.HTTP_REQUEST_TIMEOUT
import com.dixitpatel.pokemondemo.network.ApiInterface
import com.squareup.picasso.OkHttp3Downloader
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(context: Context): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = getOkHttpLogLevel(context.getString(R.string.okhttp_log_level))
        }
    }

    private fun getOkHttpLogLevel(level: String?): HttpLoggingInterceptor.Level {
        return when (level) {
            HttpLoggingInterceptor.Level.NONE.toString() -> HttpLoggingInterceptor.Level.NONE
            HttpLoggingInterceptor.Level.BASIC.toString() -> HttpLoggingInterceptor.Level.BASIC
            HttpLoggingInterceptor.Level.HEADERS.toString() -> HttpLoggingInterceptor.Level.HEADERS
            HttpLoggingInterceptor.Level.BODY.toString() -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun providesClient(cache: Cache?, loggingInterceptor: HttpLoggingInterceptor?): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor!!)
            .cache(cache)
            .build()
    }


    @Provides
    @Singleton
    fun file(app: Context): File {
        val file = File(app.applicationContext.cacheDir, "okhttp_cache")
        file.mkdirs()
        return file
    }

    @Provides
    @Singleton
    fun cache(file: File?): Cache {
        return Cache(file!!, 10 * 1000 * 1000) //10 MB
    }

    @Provides
    @Singleton
    fun okHttpDownloader(okHttpClient: OkHttpClient?): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideRetrofit(cache: Cache?, loggingInterceptor: HttpLoggingInterceptor?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(providesClient(cache, loggingInterceptor))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}