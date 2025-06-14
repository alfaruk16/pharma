package com.friendspharma.app.features.presentation.categories

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.data.remote.model.AllCategoryDto
import com.friendspharma.app.features.data.remote.model.AllCategoryDtoItem
import com.friendspharma.app.features.data.remote.model.AllCompanyDto
import com.friendspharma.app.features.data.remote.model.AllCompanyDtoItem
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import com.friendspharma.app.features.domain.use_case.GetAllCategoryUseCase
import com.friendspharma.app.features.domain.use_case.GetAllCompanyUseCase
import com.friendspharma.app.features.domain.use_case.GetCartInfoUseCase
import com.friendspharma.app.features.domain.use_case.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val getCartInfoUseCase: GetCartInfoUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase
) :
    ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        getCartInfo()
        getAllCompanies()
    }

    private fun getAllCompanies() {
        getAllCategoryUseCase.invoke().onEach { result ->
            when(result){
                is Async.Success<*> -> {
                    _state.update { it.copy(isLoading = false, categories = result.data ?: AllCategoryDto(),
                        allSearchedCategories = result.data ?: AllCategoryDto()) }
                }

                is Async.Error<*> -> {

                }
                is Async.Loading<*> -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun searchChanged(text: String, focusManager: FocusManager) {
        viewModelScope.launch {
            _state.update { it.copy(search = text) }
            if (text.isNotEmpty()) {
                val list: List<AllCategoryDtoItem> = state.value.categories.data?.filter {
                    it.CATEGORY_NAME?.lowercase()?.contains(text.lowercase()) == true
                } ?: emptyList()
                _state.update { it.copy(allSearchedCategories = state.value.allSearchedCategories.copy(data = list)) }
            } else {
                focusManager.clearFocus()
                _state.update { it.copy(allSearchedCategories = state.value.categories) }
            }
        }
    }

    private fun getCartInfo() {
        getCartInfoUseCase.invoke(sharedPreferenceHelper.getUser().MOBILE_NO ?: "")
            .onEach { result ->
                when (result) {
                    is Async.Success -> {
                        var quantity = 0
                        val set = HashSet<Int>()
                        for (item in result.data?.data ?: emptyList()) {
                            quantity += item.QUANTITY?.toInt() ?: 0
                            set.add(item.PID_PRODUCT ?: -1)
                        }
                        _state.update {
                            it.copy(
                                cartItemQuantity = quantity
                            )
                        }
                    }

                    is Async.Error -> {}
                    is Async.Loading -> {}
                }

            }.launchIn(viewModelScope)
    }

}