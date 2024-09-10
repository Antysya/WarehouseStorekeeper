package com.example.warehouse.storekeeper.api

import com.example.warehouse.storekeeper.data.LoginRequest
import com.example.warehouse.storekeeper.data.LoginResponse
import com.example.warehouse.storekeeper.data.Order
import com.example.warehouse.storekeeper.data.Product
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

interface MainApi {

    @POST("users/loginMobil")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("orders/get_order_in_progress")
    suspend fun getOrders(@Header("Authorization") token: String): Response<List<Order>>

    @GET("products-in-orders/getProductsByOrder")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("id") orderId: Int
    ): Response<List<Product>>

    //@POST("Order/{id}/confirm")
    @POST("orders/status/{id}")
    suspend fun confirmOrder(
        @Header("Authorization") token: String,
        @Path("id") orderId: Int
    ): Response<Unit>

    companion object {
        fun getInstance(): MainApi {
            val trustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf(trustManager), SecureRandom())

            val okHttpClient = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .hostnameVerifier { _, _ -> true }
                .build()

            return Retrofit.Builder()
                .baseUrl("https://192.168.1.70:7181/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MainApi::class.java)
        }
    }
}