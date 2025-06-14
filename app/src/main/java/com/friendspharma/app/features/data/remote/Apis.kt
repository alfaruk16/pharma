package com.friendspharma.app.features.data.remote

import com.friendspharma.app.MainActivity
import com.friendspharma.app.features.data.remote.entity.ChangeAddress
import com.friendspharma.app.features.data.remote.entity.ChangePassword
import com.friendspharma.app.features.data.remote.entity.DeleteAddress
import com.friendspharma.app.features.data.remote.entity.InsertAddress
import com.friendspharma.app.features.data.remote.entity.ProductAdd
import com.friendspharma.app.features.data.remote.entity.ProductRemove
import com.friendspharma.app.features.data.remote.entity.SignUp
import com.friendspharma.app.features.data.remote.entity.SignUpSeller
import com.friendspharma.app.features.data.remote.entity.SubmitOrder
import com.friendspharma.app.features.data.remote.model.AddressDto
import com.friendspharma.app.features.data.remote.model.AllCategoryDto
import com.friendspharma.app.features.data.remote.model.AllCompanyDto
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.DefaultDto
import com.friendspharma.app.features.data.remote.model.LoginDto
import com.friendspharma.app.features.data.remote.model.OrderDetailsDto
import com.friendspharma.app.features.data.remote.model.OrdersDto
import com.friendspharma.app.features.data.remote.model.ProductAddDto
import com.friendspharma.app.features.data.remote.model.ProductRemoveDto
import com.friendspharma.app.features.data.remote.model.ProductsDto
import com.friendspharma.app.features.data.remote.model.SubmitOrderDto
import com.friendspharma.app.features.data.remote.model.TokenDto
import com.friendspharma.app.features.data.remote.model.UserDetailsDto
import com.friendspharma.app.features.data.remote.model.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface Apis {

    @Headers("Content-Type: application/json")
    @GET("auth/Token")
    suspend fun getToken(): TokenDto

    @Headers("Content-Type: application/json")
    @GET("user/login")
    suspend fun loginDto(
        @Header("Authorization") token: String = MainActivity.token,
        @Query("userName") userName: String,
        @Query("passWord") passWord: String,
    ): LoginDto

    @Headers("Content-Type: application/json")
    @POST("user/insUser")
    suspend fun signUp(
        @Header("Authorization") token: String = MainActivity.token,
        @Body signUp: SignUp
    ): DefaultDto

    @Headers("Content-Type: application/json")
    @PUT("user/changePassword")
    suspend fun changePassword(
        @Header("Authorization") token: String = MainActivity.token,
        @Body changePassword: ChangePassword
    ): DefaultDto

    @Headers("Content-Type: application/json")
    @GET("product/getProduct")
    suspend fun getProducts(
        @Header("Authorization") token: String = MainActivity.token,
    ): ProductsDto

    @Headers("Content-Type: application/json")
    @POST("addcart/productadd")
    suspend fun productAdd(
        @Header("Authorization") token: String = MainActivity.token,
        @Body product: ProductAdd
    ): ProductAddDto

    @Headers("Content-Type: application/json")
    @GET("addcart/CartInfo")
    suspend fun getCartInfo(
        @Header("Authorization") token: String = MainActivity.token,
        @Query("mobile_no") mobile: String
    ): CartInfoDto

    @Headers("Content-Type: application/json")
    @POST("addcart/productRemove")
    suspend fun productRemove(
        @Header("Authorization") token: String = MainActivity.token,
        @Body productRemove: ProductRemove
    ): ProductRemoveDto

    @Headers("Content-Type: application/json")
    @POST("addcart/SubmitOrder")
    suspend fun submitOrder(
        @Header("Authorization") token: String = MainActivity.token,
        @Body submitOrder: SubmitOrder
    ): SubmitOrderDto

    @Headers("Content-Type: application/json")
    @GET("manageorder/GetOrderList")
    suspend fun getOrders(
        @Header("Authorization") token: String = MainActivity.token,
        @Query("mobile_no") mobile: String
    ): OrdersDto

    @Headers("Content-Type: application/json")
    @GET("manageorder/GetorderDetails")
    suspend fun getOrderDetails(
        @Header("Authorization") token: String = MainActivity.token,
        @Query("pid_tran_mst") id: String
    ): OrderDetailsDto

    @Headers("Content-Type: application/json")
    @GET("user/getAddress")
    suspend fun getAddress(
        @Header("Authorization") token: String = MainActivity.token,
        @Query("userid") id: String
    ): AddressDto

    @Headers("Content-Type: application/json")
    @POST("user/insAddress")
    suspend fun insertAddress(
        @Header("Authorization") token: String = MainActivity.token,
        @Body body: InsertAddress
    ): DefaultDto

    @Headers("Content-Type: application/json")
    @PUT("user/changeAddress")
    suspend fun changeAddress(
        @Header("Authorization") token: String = MainActivity.token,
        @Body body: ChangeAddress
    ): DefaultDto

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "user/deleteAddress", hasBody = true)
    suspend fun deleteAddress(
        @Header("Authorization") token: String = MainActivity.token,
        @Body body: DeleteAddress
    ): DefaultDto

    @Headers("Content-Type: application/json")
    @POST("user/insWholesaler")
    suspend fun signUpWholeSeller(
        @Header("Authorization") token: String = MainActivity.token,
        @Body signUp: SignUpSeller
    ): DefaultDto

    @Headers("Content-Type: application/json")
    @GET("user/getUser")
    suspend fun getUser(
        @Header("Authorization") token: String = MainActivity.token,
        @Query("userName") id: String
    ): UserDetailsDto

    @Headers("Content-Type: application/json")
    @GET("company/getCompany")
    suspend fun getAllCompanies(
        @Header("Authorization") token: String = MainActivity.token
    ): AllCompanyDto

    @Headers("Content-Type: application/json")
    @GET("category/getCategory")
    suspend fun getAllCategory(
        @Header("Authorization") token: String = MainActivity.token
    ): AllCategoryDto
}
