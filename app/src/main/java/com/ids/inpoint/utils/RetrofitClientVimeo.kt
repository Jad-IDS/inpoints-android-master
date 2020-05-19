package com.ids.inpoint.utils


import com.google.gson.GsonBuilder
import com.ids.inpoint.controller.MyApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClientVimeo {
    var BASE_URL = "https://player.vimeo.com/"
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(requestHeader)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }

    private/*   HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);*//* .addInterceptor(interceptor)*/ val requestHeader: OkHttpClient
        get() = OkHttpClient.Builder()
            .readTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .build()

    private fun cancelRequest() {}


}
