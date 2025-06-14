package com.friendspharma.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.NavGraph
import com.friendspharma.app.features.ScreenRoute
import com.friendspharma.app.features.domain.services.LocalConstant
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import com.friendspharma.app.features.domain.use_case.GetTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        var token = ""
    }

    @Inject
    lateinit var preferenceHelper: SharedPreferenceHelper

    @Inject
    lateinit var getTokenUseCase: GetTokenUseCase

    private val scope = CoroutineScope(Dispatchers.IO)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //FriendsPharmaTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavGraph(startDestination = ScreenRoute.Splash.route)
            }
        }
        //}

        getToken()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getToken() {
        val token = preferenceHelper.getToken()
        MainActivity.token = token
        getTokenUseCase.invoke().onEach { result ->
            when (result) {
                is Async.Success -> {
                    MainActivity.token = result.data?.data ?: ""
                    preferenceHelper.saveStringData(
                        LocalConstant.token,
                        result.data?.data ?: ""
                    )
                }

                is Async.Error -> {}
                is Async.Loading -> {}
            }
        }.launchIn(scope)
    }

//    private fun userExist(): Boolean {
//        return !preferenceHelper.getUser().MOBILE_NO.isNullOrEmpty()
//    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    //FriendsPharmaTheme {
    Greeting("Android")
    //}
}