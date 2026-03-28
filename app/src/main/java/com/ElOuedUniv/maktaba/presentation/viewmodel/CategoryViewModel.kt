package com.ElOuedUniv.maktaba.presentation.viewmodel

import com.ElOuedUniv.maktaba.domain.usecase.GetCategoriesUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing and exposing the category list state to the UI.
 * 
 * @property getCategoriesUseCase The business logic component for retrieving categories.
 */

class CategoryViewModel (private val getCategoriesUseCase: GetCategoriesUseCase ) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCategories()
    }

    /**
     * Orchestrates the loading of categories using the domain layer.
     * Updates [isLoading] state to show/hide progress indicators in the UI.
     */
     
    private fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
               // Fetch categories from the domain layer (GetCategoriesUseCase)
                val categoryList = getCategoriesUseCase()
               
                _categories.value = categoryList
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshCategories() {
        loadCategories()
    }
}
