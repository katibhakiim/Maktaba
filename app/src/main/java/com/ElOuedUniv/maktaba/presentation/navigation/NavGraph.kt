package com.ElOuedUniv.maktaba.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ElOuedUniv.maktaba.presentation.book.BookListView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookView
import com.ElOuedUniv.maktaba.presentation.book.detail.BookDetailView
import com.ElOuedUniv.maktaba.presentation.category.CategoryListView
import com.ElOuedUniv.maktaba.presentation.onboarding.OnboardingView

/**
 * TP5 – Task 3.2
 * Reads the onboarding flag from [NavGraphViewModel] (which reads DataStore) and
 * sets [NavHost]'s startDestination dynamically:
 *   - hasCompletedOnboarding == false  → Screen.Onboarding
 *   - hasCompletedOnboarding == true   → Screen.BookList
 *
 * While the flag is loading (null) we render nothing to avoid a flash.
 */
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: NavGraphViewModel = hiltViewModel()
) {
    val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState(initial = null)

    // Wait until DataStore emits before composing the NavHost to prevent
    // the wrong start destination from flashing.
    val startDestination = when (hasCompletedOnboarding) {
        true  -> Screen.BookList.route
        false -> Screen.Onboarding.route
        null  -> return  // still loading – render nothing
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingView(
                onNavigateToLibrary = {
                    navController.navigate(Screen.BookList.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.BookList.route) {
            BookListView(
                onCategoriesClick = { navController.navigate(Screen.CategoryList.route) },
                onAddBookClick = { navController.navigate(Screen.AddBook.route) },
                onBookClick = { isbn ->
                    navController.navigate(Screen.BookDetail.createRoute(isbn))
                }
            )
        }

        composable(Screen.BookDetail.route) {
            BookDetailView(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.CategoryList.route) {
            CategoryListView(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.AddBook.route) {
            AddBookView(onBackClick = { navController.popBackStack() })
        }
    }
}
