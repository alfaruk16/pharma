package com.friendspharma.app.features.domain.use_case

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.data.remote.model.AddressDto
import com.friendspharma.app.features.data.remote.model.AllCategoryDto
import com.friendspharma.app.features.domain.repository.ApiRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(private val apiRepo: ApiRepo) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    operator fun invoke(): Flow<Async<AllCategoryDto>> = flow {
        try {
            emit(Async.Loading())

            val address = apiRepo.getAllCategory()
            emit(Async.Success(address))

        } catch (e: HttpException) {
            emit(Async.Error(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Async.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}