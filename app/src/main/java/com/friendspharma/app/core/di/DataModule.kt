package com.friendspharma.app.core.di

import android.content.Context
import androidx.room.Room
import com.friendspharma.app.features.data.local.PharmaDatabase
import com.friendspharma.app.features.data.local.dao.CartDao
import com.friendspharma.app.features.data.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//@Module
//@InstallIn(SingletonComponent::class)
//abstract class RepositoryModule{
//
//    @Singleton
//    @Binds
//    abstract fun bindTrackRepo(repo: TrackRepoImpl): TrackRepo
//}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PharmaDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PharmaDatabase::class.java,
            "FriendPharma.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideProductsDao(database: PharmaDatabase): ProductDao = database.productDao()

    @Provides
    fun provideCartDao(database: PharmaDatabase): CartDao = database.cartDao()

}