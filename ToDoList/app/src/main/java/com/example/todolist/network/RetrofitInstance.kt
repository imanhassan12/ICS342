package com.example.todolist.network

import com.example.todolist.utils.BooleanAdapter
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://todos.simpleapi.dev/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(BooleanAdapter())
        .build()

//    private val client = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            val request = chain.request()
//            val response = chain.proceed(request)
//
//            // Log request details
//            Log.d("HTTP Request", request.toString())
//            Log.d("HTTP Request Body", request.body()?.toString() ?: "No body")
//
//            // Log response details
//            Log.d("HTTP Response", response.toString())
//            Log.d("HTTP Response Body", response.body()?.string() ?: "No body")
//
//            response
//        }
//        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: ToDoApiService by lazy {
        retrofit.create(ToDoApiService::class.java)
    }
}
