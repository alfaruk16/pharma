package com.friendspharma.app.features.domain.use_case

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem
import com.friendspharma.app.features.domain.repository.ProductRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetProductsByCategoryUseCase  @Inject constructor(private val productRepo: ProductRepo) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    operator fun invoke(id: Int): Flow<Async<List<ProductsDtoItem>>> = flow {
        try {
            emit(Async.Loading())

            val localProducts = productRepo.getProductsByCategory(id)
            emit(Async.Success(localProducts))

        } catch (e: HttpException) {
            emit(Async.Error(e.localizedMessage ?: "An error occurred"))
        } catch (_: IOException) {
            emit(Async.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}