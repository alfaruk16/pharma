package com.friendspharma.app.features.presentation.sign_up

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresExtension
import androidx.compose.ui.focus.FocusRequester
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.core.util.Common
import com.friendspharma.app.core.util.customer
import com.friendspharma.app.core.util.shopOwner
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.data.remote.entity.SignUp
import com.friendspharma.app.features.data.remote.entity.SignUpSeller
import com.friendspharma.app.features.domain.use_case.SignUpWholeSellerUseCase
import com.friendspharma.app.features.domain.use_case.SingUpUseCase
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SingUpUseCase,
    application: android.app.Application,
    private val signUpWholeSellerUseCase: SignUpWholeSellerUseCase
) :
    AndroidViewModel(application) {

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var geocoder: Geocoder = Geocoder(application)
    private var addresses: List<Address>? = listOf()


    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    fun checkLocationSetting(
        context: Context,
        activity: Activity,
        onDisabled: (IntentSenderRequest) -> Unit,
        onEnabled: () -> Unit
    ) {

        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)

        val gpsSettingTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())

        gpsSettingTask.addOnSuccessListener { getLocation(context, activity) }
        gpsSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()
                    onDisabled(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // ignore here
                }
            }
        }

    }

    fun getLocation(context: Context, activity: Activity) {

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        val task = fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        )
        task.addOnSuccessListener {
            if (it != null) {
                try {
                    addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    var address = ""
                    var district = ""
                    var subDistrict = ""
                    var post = ""
                    if (!addresses.isNullOrEmpty()) {
                        address = addresses?.get(0)?.getAddressLine(0) ?: ""
                        district = addresses?.get(0)?.subAdminArea ?: ""
                        post = addresses?.get(0)?.subLocality ?: ""
                    }
                    if (address.isNotEmpty()) {
                        _state.update {
                            it.copy(
                                address = address,
                                district = district,
                                post = post
                            )
                        }
                    }

                } catch (_: Exception) {
                }
            }
        }
    }


    fun userNameChanged(username: String) {
        _state.update { it.copy(userName = username) }
        validate()
    }

    fun closeSnackBar() {
        _state.update { it.copy(message = "") }
    }

    private fun validate() {
        if (state.value.userName.isEmpty() || !Common.isValidMobile(state.value.mobile) || state.value.address.isEmpty() || state.value.post.isEmpty() || state.value.district.isEmpty()
            || state.value.password.isEmpty() || state.value.password.length < 3 || state.value.password.length > 6 || state.value.confirmPassword.isEmpty() || state.value.confirmPassword.length < 2 ||
            state.value.confirmPassword.length > 6 || state.value.password != state.value.confirmPassword || state.value.userType.isEmpty() || (state.value.userType == shopOwner && state.value.drugno.isEmpty())
        ) {
            _state.update { it.copy(valid = false) }
        } else {
            _state.update { it.copy(valid = true) }
        }
    }

    fun signUp(
        mobileFocusRequester: FocusRequester,
        usernameFocusRequester: FocusRequester,
        passwordFocusRequester: FocusRequester,
        confirmPasswordFocusRequester: FocusRequester,
        addressFocusRequester: FocusRequester,
        districtFocusRequester: FocusRequester,
        subDistrictFocusRequester: FocusRequester,
        userTypeFocusRequester: FocusRequester,
        drugnoFocusRequester: FocusRequester,
        postFocusRequester: FocusRequester,
        navAction: NavigationActions,
        context: Context
    ) {
        _state.update { it.copy(isValidate = true) }
        if (state.value.userName.isEmpty()) {
            usernameFocusRequester.requestFocus()
        } else if (!Common.isValidMobile(state.value.mobile)) {
            mobileFocusRequester.requestFocus()
        } else if (state.value.address.isEmpty()) {
            addressFocusRequester.requestFocus()
        } else if (state.value.post.isEmpty()) {
            postFocusRequester.requestFocus()
        }  else if (state.value.district.isEmpty()) {
            districtFocusRequester.requestFocus()
        } else if (state.value.userType.isEmpty()) {
            userTypeFocusRequester.requestFocus()
        } else if (state.value.userType == shopOwner && state.value.drugno.isEmpty()) {
            drugnoFocusRequester.requestFocus()
        } else if (state.value.password.length < 3 || state.value.password.length > 6) {
            passwordFocusRequester.requestFocus()
        } else if (state.value.confirmPassword != state.value.confirmPassword) {
            confirmPasswordFocusRequester.requestFocus()
        } else {
            if (state.value.userType == shopOwner) {
                signUpWholeSellerUseCase.invoke(
                    SignUpSeller(
                        userName = state.value.userName,
                        mobileNo = "88" + state.value.mobile,
                        email = state.value.email,
                        passWordNo = state.value.password,
                        address = state.value.address + ", " + state.value.post.replace(",", "") + ", " + state.value.district.replace(",", ""),
                        usertype = if (state.value.userType == customer) "1" else if (state.value.userType == shopOwner) "2" else "3",
                        drugno = state.value.drugno
                    )
                ).onEach { result ->
                    when (result) {
                        is Async.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            Toast.makeText(
                                context,
                                result.data?.message ?: "Success",
                                Toast.LENGTH_LONG
                            ).show()
                            navAction.pop()
                        }

                        is Async.Error -> _state.update { it.copy(isLoading = false) }
                        is Async.Loading -> _state.update { it.copy(isLoading = true) }
                    }

                }.launchIn(viewModelScope)
            } else {
                signUpUseCase.invoke(
                    SignUp(
                        userName = state.value.userName,
                        mobileNo = "88" + state.value.mobile,
                        email = state.value.email,
                        passWordNo = state.value.password,
                        address = state.value.address + ", " + state.value.post.replace(",", "") + ", " + state.value.district.replace(",", "")
                    )
                ).onEach { result ->
                    when (result) {
                        is Async.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            Toast.makeText(
                                context,
                                result.data?.message ?: "Success",
                                Toast.LENGTH_LONG
                            ).show()
                            navAction.pop()
                        }

                        is Async.Error -> _state.update { it.copy(isLoading = false) }
                        is Async.Loading -> _state.update { it.copy(isLoading = true) }
                    }

                }.launchIn(viewModelScope)
            }
        }
    }


    fun mobileChanged(mobile: String) {
        _state.update { it.copy(mobile = mobile) }
        validate()
    }

    fun emailChanged(email: String) {
        _state.update { it.copy(email = email) }
        validate()
    }

    fun passwordChanged(pass: String) {
        _state.update { it.copy(password = pass) }
        validate()
    }

    fun showPassword() {
        _state.update { it.copy(showPassword = !state.value.showPassword) }
    }

    fun confirmPasswordChanged(conPass: String) {
        _state.update { it.copy(confirmPassword = conPass) }
        validate()
    }

    fun showConPassword() {
        _state.update { it.copy(showConfirmPassword = !state.value.showConfirmPassword) }
    }

    fun addressChanged(address: String) {
        _state.update { it.copy(address = address) }
        validate()
    }

    fun typeChanged(type: String) {
        _state.update { it.copy(userType = type) }
        validate()
    }

    fun drugNoChanged(drugNo: String) {
        _state.update { it.copy(drugno = drugNo) }
        validate()
    }

    fun districtChanged(string: String) {
        _state.update { it.copy(district = string) }
        validate()
    }

    fun postChanged(string: String) {
        _state.update { it.copy(post = string) }
        validate()
    }

}