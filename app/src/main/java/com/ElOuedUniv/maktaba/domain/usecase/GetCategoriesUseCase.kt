package com.ElOuedUniv.maktaba.domain.usecase

import com.ElOuedUniv.maktaba.domain.usecase.GetCategoriesUseCase
import com.ElOuedUniv.maktaba.data.model.Category
import com.ElOuedUniv.maktaba.data.repository.CategoryRepository

/**
 * Use case to retrieve all available book categories from the library system.
 * 
 * Acts as the bridge between the UI/ViewModel and the Data layer, 
 * ensuring that data access follows the app's business rules.
 */

// TODO: Implement this use case
class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
       /**
     * Executes the use case to fetch the categories list.
     * 
     * Using the 'operator' keyword allows calling this use case directly 
     * as a function: getCategoriesUseCase().
     * 
     * @return A list of all available [Category] objects.
     */
    
    operator fun invoke(): List<Category> {
     return categoryRepository.getAllCategories()
    }
}
