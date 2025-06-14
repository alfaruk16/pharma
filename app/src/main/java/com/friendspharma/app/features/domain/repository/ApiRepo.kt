package com.friendspharma.app.features.domain.repository

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


interface ApiRepo {

    suspend fun getToken(): TokenDto
    suspend fun login(userName: String, password: String): LoginDto
    suspend fun signUp(signUp: SignUp): DefaultDto
    suspend fun changePassword(changePassword: ChangePassword): DefaultDto
    suspend fun getProducts(): ProductsDto
    suspend fun productAdd(product: ProductAdd): ProductAddDto
    suspend fun getCartInfo(mobile: String): CartInfoDto
    suspend fun productRemove(productRemove: ProductRemove): ProductRemoveDto
    suspend fun submitOrder(submitOrder: SubmitOrder): SubmitOrderDto
    suspend fun getOrders(mobile: String): OrdersDto
    suspend fun getOrderDetails(id: String): OrderDetailsDto
    suspend fun getAddress(id: String): AddressDto
    suspend fun insertAddress(body: InsertAddress): DefaultDto
    suspend fun changeAddress(body: ChangeAddress): DefaultDto
    suspend fun deleteAddress(body: DeleteAddress): DefaultDto
    suspend fun signUpWholeSeller(signUp: SignUpSeller): DefaultDto
    suspend fun getUser(id: String): UserDetailsDto
    suspend fun getAllCompany() : AllCompanyDto
    suspend fun getAllCategory(): AllCategoryDto

}