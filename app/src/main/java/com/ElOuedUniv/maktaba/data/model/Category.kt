package com.ElOuedUniv.maktaba.data.model

// TODO: Complete the Category data class implementation

/**

 * Represents a library genre or category  of organiziting books.
 *
 * @property id Unuque  identifier for category.
 * @property name Display name shown to the user.
 * @property description detailed summary of the category`s purpose.
 *

 */

data class Category(

// * @property id Unuque  identifier for category.
 val id          : string ,

// * @property name Display name shown to the user.
 val name        : string ,

// * @property description detailed summary of the category`s purpose.
 val description : string

)
