@file:OptIn(ExperimentalAnimationApi::class)

package com.example.cointrack.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import com.example.cointrack.ui.navigation.Routes.LOG_IN_SCREEN
import com.example.cointrack.ui.navigation.Routes.ON_BOARDING_SCREEN
import com.example.cointrack.ui.navigation.Routes.SIGN_UP_SCREEN
import com.example.cointrack.ui.navigation.Routes.SPLASH_SCREEN
import com.example.cointrack.ui.screens.account.login.LogInScreen
import com.example.cointrack.ui.screens.account.onboarding.OnBoardingScreen
import com.example.cointrack.ui.screens.account.signup.SignUpScreen
import com.example.cointrack.ui.screens.account.splash.SplashScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun AccountActivityNavigation() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN,
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
            route = SPLASH_SCREEN
        ) {
            SplashScreen(navController)
        }

        composable(
            route = ON_BOARDING_SCREEN
        ) {
            OnBoardingScreen(navController)
        }

        composable(
            route = LOG_IN_SCREEN
        ) {
            LogInScreen(navController)
        }

        composable(
            route = SIGN_UP_SCREEN
        ) {
            SignUpScreen(navController)
        }
    }
}