package com.healthcoco.safeplace.network

import android.text.TextUtils
import com.example.sampletestsearchimage.BuildConfig
import com.example.sampletestsearchimage.network.CustomResponse
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.Converter
import java.lang.reflect.Type


object Networking {

    const val HEADER_API_KEY = "x-api-key"
    const val HEADER_ACCESS_TOKEN = "x-access-token"
    const val HEADER_USER_ID = "x-user-id"

    private const val NETWORK_CALL_TIMEOUT = 60
    internal lateinit var API_KEY: String

    // Create a Custom Interceptor to apply Headers application wide
    val headerInterceptor = object : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Client-ID 137cda6b5008a7c")
                .build()
            val response = chain.proceed(request)
            return response
        }
    }

    val customConverter = object : Converter.Factory() {
        override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {

            return   object : Converter<ResponseBody,Any>{
                override fun convert(value: ResponseBody):Any{
                    var gson = Gson()
                   var response = gson.fromJson(value.string(), CustomResponse::class.java)

                    response?.data = (this@Networking.getObjectFromLinkedTreeMap(gson,type.javaClass,response.data) as Array<Any>?)!!

                    return response!!
                }

            }
        }
    }

    /**
     * converting a linked map object to the specified class object
     *
     * @param gson      : Gson object for parsing
     * @param classType : response class
     * @param object    : object that is to be converted
     */
    fun getObjectFromLinkedTreeMap(gson: Gson?, classType: Class<*>, `object`: Any): Any? {
        var gson = gson
        try {
            if (gson == null)
                gson = Gson()
            var dataJson: String
            if (`object` is String)
                dataJson = gson.toJson(`object`, String::class.java)
            else if (`object` is Map<*, *>)
                dataJson = gson.toJson(`object`,Map::class.java)
            else
                dataJson = gson.toJson(`object`, classType)
            if (!TextUtils.isEmpty(dataJson)) {
                return gson.fromJson(dataJson, classType)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // Create a Custom Authorization to apply Headers application wide
    val authenticator = object : Authenticator {
        override fun authenticate(route: Route, response: Response): Request? {
            var newAccessToken: String


            if (response.code() === 401) { //if unauthorized
                synchronized(this) {
                    //perform all 401 in sync blocks, to avoid multiply token updates


                }

            }


            return response.request().newBuilder()
                .header("Authorization", "")
                .build();

        }
    }




        val retrofitBuilder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(
                    OkHttpClient.Builder()
                      //  .authenticator(authenticator)
                        .addInterceptor(headerInterceptor)
                        .addInterceptor(HttpLoggingInterceptor()
                            .apply {
                                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                                else HttpLoggingInterceptor.Level.NONE
                            })
                        .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        }

        val apiService: NetworkService by lazy {
            retrofitBuilder
                .build()
                .create(NetworkService::class.java)

        }


    }