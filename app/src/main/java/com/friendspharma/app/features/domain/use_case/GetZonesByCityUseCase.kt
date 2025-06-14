package com.friendspharma.app.features.domain.use_case

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.data.remote.model.CityDto
import com.friendspharma.app.features.data.remote.model.ZoneDto
import com.friendspharma.app.features.domain.repository.PathaoApiRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetZonesByCityUseCase @Inject constructor(private val pathaoApiRepo: PathaoApiRepo) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    operator fun invoke(token: String, city: String): Flow<Async<ZoneDto>> = flow {
        try {
            emit(Async.Loading())
            val order = pathaoApiRepo.getZoneByCity(token, city)
            emit(Async.Success(order))
        } catch (e: HttpException) {
            emit(Async.Error(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Async.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}