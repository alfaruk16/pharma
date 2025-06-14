package com.friendspharma.app.core.di

import com.friendspharma.app.features.data.local.dao.CartDao
import com.friendspharma.app.features.data.local.dao.ProductDao
import com.friendspharma.app.features.data.remote.Apis
import com.friendspharma.app.features.data.remote.PathaoApis
import com.friendspharma.app.features.data.remote.SteadFastApis
import com.friendspharma.app.features.data.repository.ApiRepoImpl
import com.friendspharma.app.features.data.repository.CartRepoImpl
import com.friendspharma.app.features.data.repository.PathaoApiRepoImpl
import com.friendspharma.app.features.data.repository.ProductRepoImpl
import com.friendspharma.app.features.data.repository.SteadFastApiRepoImpl
import com.friendspharma.app.features.domain.repository.ApiRepo
import com.friendspharma.app.features.domain.repository.CartRepo
import com.friendspharma.app.features.domain.repository.PathaoApiRepo
import com.friendspharma.app.features.domain.repository.ProductRepo
import com.friendspharma.app.features.domain.repository.SteadFastApiRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://api/"
    private const val PATHAO_BASE_URL = "https://api/"
    private const val STEAD_FAST_BASE_URL = "https://api/"

    @Provides
    @Singleton
    fun provideApi(): Apis {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Apis::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRepo(api: Apis): ApiRepo {
        return ApiRepoImpl(api)
    }

    @Provides
    @Singleton
    fun providePathaoApi(): PathaoApis {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
        }.build()

        return Retrofit.Builder().baseUrl(PATHAO_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PathaoApis::class.java)
    }

    @Provides
    @Singleton
    fun providePathaoApiRepo(pathaoApis: PathaoApis): PathaoApiRepo {
        return PathaoApiRepoImpl(pathaoApis)
    }

    @Provides
    @Singleton
    fun provideSteadFastApi(): SteadFastApis {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
        }.build()

        return Retrofit.Builder().baseUrl(STEAD_FAST_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SteadFastApis::class.java)
    }

    @Provides
    @Singleton
    fun provideSteadFastApiRepo(steadFastApis: SteadFastApis) : SteadFastApiRepo {
        return SteadFastApiRepoImpl(steadFastApis)
    }

    @Provides
    @Singleton
    fun provideProductRepo(productDao: ProductDao): ProductRepo {
        return ProductRepoImpl(
            productDao,
            CoroutineModule.provideDefaultDispatcher(),
            CoroutineModule.provideCoroutineScope(CoroutineModule.provideDefaultDispatcher())
        )
    }

    @Provides
    @Singleton
    fun provideCartRepo(cartDao: CartDao): CartRepo {
        return CartRepoImpl(
            cartDao,
            CoroutineModule.provideDefaultDispatcher(),
            CoroutineModule.provideCoroutineScope(CoroutineModule.provideDefaultDispatcher())
        )
    }

}