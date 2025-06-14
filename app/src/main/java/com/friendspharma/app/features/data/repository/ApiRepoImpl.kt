package com.friendspharma.app.features.data.repository

import com.friendspharma.app.features.data.remote.Apis
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
import com.friendspharma.app.features.domain.repository.ApiRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepoImpl @Inject constructor(
    private val api: Apis
) : ApiRepo {
    override suspend fun getToken(): TokenDto {
        return api.getToken()
    }

    override suspend fun login(userName: String, password: String): LoginDto {
        return api.loginDto(userName = userName, passWord = password)
    }

    override suspend fun signUp(signUp: SignUp): DefaultDto {
        return api.signUp(signUp = signUp)
    }

    override suspend fun changePassword(changePassword: ChangePassword): DefaultDto {
        return api.changePassword(changePassword = changePassword)
    }

    override suspend fun getProducts(): ProductsDto {
        return api.getProducts()
    }

    override suspend fun productAdd(product: ProductAdd): ProductAddDto {
        return api.productAdd(product = product)
    }

    override suspend fun getCartInfo(mobile: String): CartInfoDto {
        return api.getCartInfo(mobile = mobile)
    }

    override suspend fun productRemove(productRemove: ProductRemove): ProductRemoveDto {
        return api.productRemove(productRemove = productRemove)
    }

    override suspend fun submitOrder(submitOrder: SubmitOrder): SubmitOrderDto {
        return api.submitOrder(submitOrder = submitOrder)
    }

    override suspend fun getOrders(mobile: String): OrdersDto {
        return api.getOrders(mobile = mobile)
    }

    override suspend fun getOrderDetails(id: String): OrderDetailsDto {
        return api.getOrderDetails(id = id)
    }

    override suspend fun getAddress(id: String): AddressDto {
        return api.getAddress(id = id)
    }

    override suspend fun insertAddress(
        body: InsertAddress
    ): DefaultDto {
        return api.insertAddress(body = body)
    }

    override suspend fun changeAddress(
        body: ChangeAddress
    ): DefaultDto {
        return api.changeAddress(body = body)
    }

    override suspend fun deleteAddress(body: DeleteAddress): DefaultDto {
        return api.deleteAddress(body = body)
    }

    override suspend fun signUpWholeSeller(signUp: SignUpSeller): DefaultDto {
        return api.signUpWholeSeller(signUp = signUp)
    }

    override suspend fun getUser(id: String): UserDetailsDto {
        return api.getUser(id = id)
    }

    override suspend fun getAllCompany(): AllCompanyDto {
        return api.getAllCompanies()
    }

    override suspend fun getAllCategory(): AllCategoryDto {
        return api.getAllCategory()
    }

}