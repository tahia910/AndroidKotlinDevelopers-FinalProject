package com.example.android.nextreminder.data.network

import com.example.android.nextreminder.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://tastedive.com/api/"

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

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


object ApiClient {
    val retrofitService: SimilarService by lazy {
        retrofit.create(SimilarService::class.java)
    }
}