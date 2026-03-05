package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Category

/**
 * Concrete implementation of the [CategoryRepository].
 * 
 * Provides book categories from a static, hardcoded list. This implementation 
 * is suitable for initial development or when categories are constant.
 */

class CategoryRepositoryImpl : CategoryRepository {

     /**
     * Internal data source containing the available library categories.
     */
    
    private val categoriesList = listOf<Category>(  
        Category("1", "Programming", "Books about software development and coding"),
        Category("2", "Algorithms", "Books about algorithms and data structures"),
        Category("3", "Databases", "Books about database design and management"),
        Category("4", "Mobile Development", "Books about Android and iOS development"),
        Category("5", "Cybersecurity", "Books about network security and hacking")
    )

        /**
     * Retrieves all categories defined in the library system.
     * @return A list of [Category] objects.
     */
     
    override fun getAllCategories(): List<Category> {
            return categoriesList
    }
    
 /**
     * Searches for a category matching the provided unique identifier.
     * @param id The unique ID of the category to find.
     * @return The matching [Category], or null if no category exists with that ID.
     */
     
    override fun getCategoryById(id: String): Category? {
       return categoriesList.find { it.id == id }
    }
}
