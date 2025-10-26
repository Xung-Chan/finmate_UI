package com.example.ibanking_kltn.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.ibanking_kltn.data.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import jakarta.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    val baseUrl = "https://nsqxjbdt-9000.asse.devtunnels.ms/"
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("auth_preferences", Context.MODE_PRIVATE)
    }

//    @Singleton
//    @Provides
//    fun provideOkHttpClient(
//        interceptor: AuthInterceptor,
//        authenticator: TokenAuthenticator
//    ): OkHttpClient {
//        return OkHttpClient
//            .Builder()
//            .addInterceptor(interceptor)
//            .authenticator(authenticator)
//            .build()
//    }

//    @Singleton
//    @Provides
//    @Named("AuthRetrofit")
//    fun provideAuthRetrofitInstance(
//        @ApplicationContext context: Context,
//        sharedPreferences: SharedPreferences,
//        okHttpClient: OkHttpClient
//    ): Retrofit {
//
//        return Retrofit.Builder()
//
//            .baseUrl(baseUrl)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    @Singleton
    @Provides
    @Named("NonAuthRetrofit")
    fun provideNonAuthRetrofitInstance(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        @Named("NonAuthRetrofit")
        retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}