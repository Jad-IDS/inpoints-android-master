package com.ids.inpoint.utils


import com.google.gson.GsonBuilder
import com.ids.inpoint.controller.MyApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClientAuth {
    var BASE_URL ="http://demo.ids.com.lb/inPoint/api/"
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

    private
    val requestHeader: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor{ it.proceed(it.request().newBuilder()
                .addHeader("Authorization", MyApplication.userLoginInfo.token.toString())
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build())}
            .readTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .build()

    private fun cancelRequest() {}


}
