package com.friendspharma.app.features.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.friendspharma.app.features.data.local.dao.CartDao
import com.friendspharma.app.features.data.local.dao.ProductDao
import com.friendspharma.app.features.data.local.entities.LocalCartItem
import com.friendspharma.app.features.data.local.entities.LocalProductItem


@Database(
    entities = [LocalProductItem::class, LocalCartItem::class],
    version = 1, exportSchema = false
)
abstract class PharmaDatabase : RoomDatabase() {

    abstract fun productDao() : ProductDao

    abstract fun cartDao(): CartDao
}