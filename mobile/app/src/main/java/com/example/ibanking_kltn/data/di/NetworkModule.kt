package com.example.ibanking_kltn.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.ibanking_kltn.BuildConfig
import com.example.ibanking_kltn.data.api.AuthApi
import com.example.ibanking_kltn.data.api.BillApi
import com.example.ibanking_kltn.data.api.NonAuthApi
import com.example.ibanking_kltn.data.api.PayLaterApi
import com.example.ibanking_kltn.data.api.TransactionApi
import com.example.ibanking_kltn.data.api.WalletApi
import com.example.ibanking_kltn.data.security.TokenAuthenticator
import com.example.ibanking_soa.data.security.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val GATEWAY_URL = BuildConfig.GATEWAY_URL
    const val SHARED_PREFS_KEY = BuildConfig.SHARED_PREFS_KEY


    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPrefs = EncryptedSharedPreferences.create(
            context, SHARED_PREFS_KEY, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPrefs
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptor: AuthInterceptor,
        authenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .authenticator(authenticator)
            .build()
    }

    //////////////////////

    @Singleton
    @Provides
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences,
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(GATEWAY_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    @Named("NonAuthRetrofit")
    fun provideNonAuthRetrofit(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(GATEWAY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    fun provideNonAuthApi(
        @Named("NonAuthRetrofit")
        retrofit: Retrofit
    ): NonAuthApi {
        return retrofit.create(NonAuthApi::class.java)

    }

    @Singleton
    @Provides
    fun provideAuthApi(
        @Named("AuthRetrofit")
        retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWalletApi(
        @Named("AuthRetrofit")
        retrofit: Retrofit
    ): WalletApi {
        return retrofit.create(WalletApi::class.java)
    }


    @Singleton
    @Provides
    fun provideTransactionApi(
        @Named("AuthRetrofit")
        retrofit: Retrofit
    ): TransactionApi {
        return retrofit.create(TransactionApi::class.java)

    }


    @Singleton
    @Provides
    fun providePayLaterApi(
        @Named("AuthRetrofit")
        retrofit: Retrofit
    ): PayLaterApi {
        return retrofit.create(PayLaterApi::class.java)

    }

    @Singleton
    @Provides
    fun provideBillApi(
        @Named("AuthRetrofit")
        retrofit: Retrofit
    ): BillApi {
        return retrofit.create(BillApi::class.java)

    }


}