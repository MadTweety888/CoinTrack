@file:OptIn(ExperimentalAnimationApi::class)

package com.example.cointrack.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cointrack.ui.activities.MainActivityViewModel
import com.example.cointrack.ui.navigation.Routes.PROFILE_SCREEN
import com.example.cointrack.ui.navigation.Routes.STATISTICS_SCREEN
import com.example.cointrack.ui.navigation.Routes.TRANSACTIONS_SCREEN
import com.example.cointrack.ui.screens.main.profile.ProfileScreen
import com.example.cointrack.ui.screens.main.statistics.StatisticsScreen
import com.example.cointrack.ui.screens.main.transactions.TransactionsScreen
import com.example.cointrack.ui.util.components.bottomnav.BottomNavBar
import com.example.cointrack.ui.util.components.bottomnav.BottomNavItem
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun MainActivityLayoutAndNavigation() {

    val navController = rememberAnimatedNavController()
    val viewModel = hiltViewModel<MainActivityViewModel>()

    NavHostAndBottomNavigation(navController, viewModel)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun NavHostAndBottomNavigation(
    navController: NavHostController,
    viewModel: MainActivityViewModel
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavBarVisibilityState = rememberSaveable { viewModel.bottomNavBarVisibilityState }

    viewModel.setBottomNavBarVisibilityState(currentRoute)

    val bottomNavBarItems = listOf(
        BottomNavItem(
            route = TRANSACTIONS_SCREEN,
            icon = Icons.Default.ReceiptLong
        ),
        BottomNavItem(
            route = STATISTICS_SCREEN,
            icon = Icons.Default.BarChart
        ),
        BottomNavItem(
            route = PROFILE_SCREEN,
            icon = Icons.Default.Person
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                items = bottomNavBarItems,
                bottomBarState = bottomNavBarVisibilityState.value,
                onItemClick = { navController.navigateToSelectedBottomNavBarRoute(it.route) }
            )
        }
    ) {

        AnimatedNavigation(navController, viewModel)
    }
}

@Composable
private fun AnimatedNavigation(
    navController: NavHostController,
    mainViewModel: MainActivityViewModel
) {

    AnimatedNavHost(
        navController = navController,
        startDestination = TRANSACTIONS_SCREEN,
        enterTransition = {
            fadeIn(
                initialAlpha = 0.1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        }
    ) {

        composable(
            route = TRANSACTIONS_SCREEN
        ) {
            TransactionsScreen(navController, mainViewModel)
        }

        composable(
            route = STATISTICS_SCREEN
        ) {
            StatisticsScreen(navController)
        }

        composable(
            route = PROFILE_SCREEN
        ) {
            ProfileScreen(navController)
        }
    }
}

private fun NavHostController.navigateToSelectedBottomNavBarRoute(route: String) {

    val navController = this

    navController.navigate(route) { setBottomBarNavigationOptions(navController) }
}

private fun NavOptionsBuilder.setBottomBarNavigationOptions(navController: NavHostController) {

    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
}