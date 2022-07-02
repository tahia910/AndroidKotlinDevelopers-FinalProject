package com.example.android.nextreminder.data.network

import com.example.android.nextreminder.BuildConfig
import com.example.android.nextreminder.data.network.image.ImageService
import com.example.android.nextreminder.data.network.similar.SimilarService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val SIMILAR_BASE_URL = "https://tastedive.com/"
private const val IMAGE_BASE_URL = "https://imsea.herokuapp.com/"

private val logging = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    // Add the API key to every request (instead of adding it as a parameter each time)
    // https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request
    .addInterceptor { chain ->
        val original = chain.request()
        val originalUrl = original.url()
        val newUrl = originalUrl.newBuilder().addQueryParameter("k", BuildConfig.API_KEY).build()
        val request = original.newBuilder().url(newUrl).build()
        chain.proceed(request)
    }
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val similarRetrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(SIMILAR_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

private val imageRetrofit = Retrofit.Builder()
    .client(OkHttpClient.Builder().build())
    .baseUrl(IMAGE_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

object ApiClient {
    val similarService: SimilarService by lazy {
        similarRetrofit.create(SimilarService::class.java)
    }

    val imageService: ImageService by lazy {
        imageRetrofit.create(ImageService::class.java)
    }
}