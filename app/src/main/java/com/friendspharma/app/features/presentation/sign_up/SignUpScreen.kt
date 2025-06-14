package com.friendspharma.app.features.presentation.sign_up

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friendspharma.app.R
import com.friendspharma.app.core.components.AppBar
import com.friendspharma.app.core.components.AppName
import com.friendspharma.app.core.components.ButtonK
import com.friendspharma.app.core.components.Loader
import com.friendspharma.app.core.components.TextFieldK
import com.friendspharma.app.core.util.Common
import com.friendspharma.app.core.util.KeyboardUnFocusHandler
import com.friendspharma.app.core.util.shopOwner
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.presentation.sign_up.components.UserTypeDialogue
import kotlinx.coroutines.CoroutineScope

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navAction: NavigationActions,
    scope: CoroutineScope = rememberCoroutineScope()
) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.sign_up),
                navAction = navAction
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        val state by viewModel.state.collectAsStateWithLifecycle()
        val width = LocalConfiguration.current.screenWidthDp
        val usernameFocusRequester = FocusRequester()
        val mobileFocusRequester = FocusRequester()
        val emailFocusRequester = FocusRequester()
        val passwordFocusRequester = FocusRequester()
        val confirmPassFocusRequester = FocusRequester()
        val addressFocusRequester = FocusRequester()
        val districtFocusRequester = FocusRequester()
        val subDistrictFocusRequester = FocusRequester()
        val postFocusRequester = FocusRequester()
        val typeFocusRequester = FocusRequester()
        val drugNoFocusRequester = FocusRequester()
        val context = LocalContext.current
        val openTypeDialog = remember { mutableStateOf(false) }
        val activity = LocalContext.current as Activity

        KeyboardUnFocusHandler()


        val settingResultRequest = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK)
                viewModel.getLocation(context, activity)
            else {
                println("Denied")
            }
        }

        val locationPermissionResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {
                if (it) {
                    viewModel.checkLocationSetting(
                        context = context,
                        activity = activity,
                        onDisabled = { intentSenderRequest ->
                            settingResultRequest.launch(intentSenderRequest)
                        },
                        onEnabled = { viewModel.getLocation(context, activity) }
                    )

                }
            }
        )

        LaunchedEffect(Unit) {
            locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }


        when {
            openTypeDialog.value ->
                UserTypeDialogue(
                    onDismiss = {
                        openTypeDialog.value = false
                    },
                    onSelected = {
                        viewModel.typeChanged(it)
                        openTypeDialog.value = false
                    }
                )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.White)

        ) {

            Column(
                Modifier
                    .padding(horizontal = 10.dp)
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_small),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 20.dp, top = 20.dp)
                        .size((width / 4).dp)
                )

                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    TextFieldK(
                        value = state.userName,
                        onValueChange = { viewModel.userNameChanged(it) },
                        focusRequester = usernameFocusRequester,
                        label = R.string.user_name,
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        keyboardType = KeyboardType.Text,
                        error = if (state.isValidate && state.userName.isEmpty()) stringResource(
                            id = R.string.enter_username
                        ) else "",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    TextFieldK(
                        value = state.mobile,
                        onValueChange = { viewModel.mobileChanged(it) },
                        focusRequester = mobileFocusRequester,
                        leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                        label = R.string.mobile_number,
                        keyboardType = KeyboardType.Phone,
                        error = if (state.isValidate && !Common.isValidMobile(state.mobile)) stringResource(
                            id = R.string.enter_valid_mobile_number
                        ) + " ${state.userName.length}/11" else "",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    TextFieldK(
                        value = state.email,
                        onValueChange = { viewModel.emailChanged(it) },
                        focusRequester = emailFocusRequester,
                        label = R.string.email,
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    TextFieldK(
                        value = state.address,
                        onValueChange = { viewModel.addressChanged(it) },
                        focusRequester = addressFocusRequester,
                        label = R.string.address,
                        leadingIcon = { Icon(Icons.Filled.AddLocation, contentDescription = null) },
                        keyboardType = KeyboardType.Text,
                        error = if (state.isValidate && state.address.isEmpty()) stringResource(
                            id = R.string.enter_address
                        ) else "",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    TextFieldK(
                        value = state.post,
                        onValueChange = { viewModel.postChanged(it) },
                        focusRequester = postFocusRequester,
                        label = R.string.post,
                        leadingIcon = { Icon(Icons.Filled.AddLocation, contentDescription = null) },
                        keyboardType = KeyboardType.Text,
                        error = if (state.isValidate && state.post.isEmpty()) stringResource(
                            id = R.string.enter_post
                        ) else "",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    TextFieldK(
                        value = state.district,
                        onValueChange = { viewModel.districtChanged(it) },
                        focusRequester = districtFocusRequester,
                        label = R.string.dristict,
                        leadingIcon = { Icon(Icons.Filled.AddLocation, contentDescription = null) },
                        keyboardType = KeyboardType.Text,
                        error = if (state.isValidate && state.district.isEmpty()) stringResource(
                            id = R.string.enter_district
                        ) else "",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    TextFieldK(
                        value = state.userType,
                        focusRequester = typeFocusRequester,
                        onValueChange = {},
                        leadingIcon = { Icon(Icons.Filled.Category, contentDescription = null) },
                        label = R.string.user_type,
                        error = if (state.isValidate && state.userType.isEmpty()) stringResource(
                            id = R.string.select_user_type
                        ) else "",
                        onTap = { openTypeDialog.value = true },
                        enabled = false,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    if (state.userType == shopOwner)
                        TextFieldK(
                            value = state.drugno,
                            focusRequester = drugNoFocusRequester,
                            onValueChange = { viewModel.drugNoChanged(it) },
                            leadingIcon = { Icon(Icons.Filled.Numbers, contentDescription = null) },
                            label = R.string.drug_no,
                            error = if (state.isValidate && state.drugno.isEmpty()) stringResource(
                                id = R.string.enter_drug_no
                            ) else "",
                            modifier = Modifier.padding(vertical = 6.dp)
                        )


                    TextFieldK(
                        value = state.password,
                        label = R.string.enter_password,
                        focusRequester = passwordFocusRequester,
                        onValueChange = { viewModel.passwordChanged(it) },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        error = if (state.isValidate && state.password.isEmpty()) stringResource(
                            id = R.string.enter_password
                        ) else if (state.isValidate && (state.password.length < 3 || state.password.length > 6)) stringResource(
                            id = R.string.password_should_be
                        ) else "",
                        visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.padding(vertical = 6.dp),
                        suffixIcon = {
                            Icon(if (state.showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.showPassword()
                                    })
                        }
                    )

                    TextFieldK(
                        value = state.confirmPassword,
                        label = R.string.re_enter_password,
                        focusRequester = confirmPassFocusRequester,
                        onValueChange = { viewModel.confirmPasswordChanged(it) },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        error = if (state.isValidate && state.confirmPassword.isEmpty()) stringResource(
                            id = R.string.re_enter_password
                        ) else if (state.isValidate && (state.confirmPassword.length < 3 || state.confirmPassword.length > 6)) stringResource(
                            id = R.string.re_enter_password_error
                        )
                        else if (state.isValidate && state.password != state.confirmPassword) stringResource(
                            id = R.string.password_didnt_match
                        )
                        else "",
                        visualTransformation = if (state.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.padding(vertical = 6.dp),
                        suffixIcon = {
                            Icon(if (state.showConfirmPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.showConPassword()
                                    })
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                    ButtonK(
                        text = R.string.sign_up,
                        isLoading = state.isLoading,
                        isValid = state.valid
                    ) {
                        viewModel.signUp(
                            usernameFocusRequester = usernameFocusRequester,
                            mobileFocusRequester = mobileFocusRequester,
                            passwordFocusRequester = passwordFocusRequester,
                            confirmPasswordFocusRequester = confirmPassFocusRequester,
                            addressFocusRequester = addressFocusRequester,
                            districtFocusRequester = districtFocusRequester,
                            subDistrictFocusRequester = subDistrictFocusRequester,
                            postFocusRequester = postFocusRequester,
                            userTypeFocusRequester = typeFocusRequester,
                            drugnoFocusRequester = drugNoFocusRequester,
                            navAction = navAction,
                            context = context
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

            }

            AppName()
        }


        if (state.isLoading)
            Loader(paddingValues = paddingValues)

    }
}