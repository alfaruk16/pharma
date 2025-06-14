package com.friendspharma.app.features.domain.use_case

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.data.remote.model.ProductsDto
import com.friendspharma.app.features.domain.repository.ApiRepo
import com.friendspharma.app.features.domain.repository.ProductRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val apiRepo: ApiRepo,
    private val productRepo: ProductRepo
) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    operator fun invoke(isForceRefresh: Boolean = false): Flow<Async<ProductsDto>> = flow {
        try {
            emit(Async.Loading())

            val localProducts = productRepo.getProducts()
            if (localProducts.isNotEmpty()) {
                emit(Async.Success(ProductsDto(data = localProducts)))
            }

            if (isForceRefresh || localProducts.isEmpty()) {
                val products = apiRepo.getProducts()
                emit(Async.Success(products))

                productRepo.deleteAllProducts()
                productRepo.createProducts(products.data ?: emptyList())
            }
        } catch (e: HttpException) {
            emit(Async.Error(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Async.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}