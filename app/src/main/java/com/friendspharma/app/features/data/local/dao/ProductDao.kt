package com.friendspharma.app.features.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.friendspharma.app.features.data.local.entities.LocalProductItem
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    suspend fun getAll(): List<LocalProductItem>

    @Query("SELECT * FROM product WHERE PID_PRODUCT = :id")
    suspend fun getById(id: String): LocalProductItem?

    @Upsert
    suspend fun upsert(track: LocalProductItem)

    @Upsert
    suspend fun upsertAll(tracks: List<LocalProductItem>)

    @Query("DELETE FROM product WHERE PID_PRODUCT = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM product")
    suspend fun deleteAll()

    @Query("SELECT * FROM product WHERE PID_COMPANY = :id")
    suspend fun getProductsByCompany(id: Int): List<ProductsDtoItem>

    @Query("SELECT * FROM product WHERE PID_CATEGORY = :id")
    suspend fun getProductsByCategory(id: Int): List<ProductsDtoItem>
}