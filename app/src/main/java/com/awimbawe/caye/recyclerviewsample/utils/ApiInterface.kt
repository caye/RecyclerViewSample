package com.awimbawe.caye.recyclerviewsample.utils

import com.awimbawe.caye.recyclerviewsample.model.entity.Item
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

/**
* This interface contains all the HTTP requests endpoints to be called. It's using Retrofit with GSON so in order to
* use it you should declare it like:
* <pre>
*     disposable = api.requestItems()
*     .subscribeOn(Schedulers.io())
*     .observeOn(AndroidSchedulers.mainThread())
*     .subscribe(
*      { result -> Toast.makeText(this, "Items fetched", Toast.LENGTH_SHORT).show()},
*      { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
*      )
* </pre>
*
* Before using this class, please set SERVER_IP to the right address where you are running the API
*/
interface ApiInterface {
    @Headers("Accept: application/json")
    @GET("81be3bb0?count=200&key=8d38c780")
    fun requestItems(): Observable<Collection<Item>>

    companion object {
        val SERVER_IP = "api.mockaroo.com"
        val BASE_URL = "https://"+SERVER_IP+"/api/"
        fun create(): ApiInterface {
            val gson = GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}