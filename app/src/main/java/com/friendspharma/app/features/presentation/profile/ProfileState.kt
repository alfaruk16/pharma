package com.friendspharma.app.features.presentation.profile

import com.friendspharma.app.features.data.remote.model.UserDto

data class ProfileState(
    val phone: String = "",
    val user: UserDto = UserDto()
)
