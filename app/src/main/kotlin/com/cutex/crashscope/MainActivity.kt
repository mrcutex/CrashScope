package com.cutex.crashscope

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cutex.crashscope.data.SettingsManager
import com.cutex.crashscope.ui.detail.CrashDetailScreen
import com.cutex.crashscope.ui.home.HomeScreen
import com.cutex.crashscope.ui.home.HomeViewModel
import com.cutex.crashscope.ui.onboarding.OnboardingScreen
import com.cutex.crashscope.ui.permission.PermissionScreen
import com.cutex.crashscope.ui.settings.SettingsScreen
import com.cutex.crashscope.ui.settings.AboutScreen
import com.cutex.crashscope.ui.theme.CrashScopeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settings = SettingsManager(this)
        
        
        val hasPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_LOGS
        ) == PackageManager.PERMISSION_GRANTED

        val startDestination = when {
            !settings.isOnboardingCompleted -> "onboarding"
            !hasPermission -> "permission"
            else -> "home"
        }

        setContent {
            CrashScopeTheme {
                val navController = rememberNavController()
                val viewModel: HomeViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400))
                    },
                    exitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400))
                    },
                    popEnterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400))
                    },
                    popExitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400))
                    }
                ) {
                    
                    
                    composable("onboarding") {
                        OnboardingScreen(onFinished = {
                            settings.isOnboardingCompleted = true
                            val nextDest = if (hasPermission) "home" else "permission"
                            navController.navigate(nextDest) {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        })
                    }

                    
                    composable("permission") {
                        PermissionScreen(onPermissionGranted = {
                            navController.navigate("home") {
                                popUpTo("permission") { inclusive = true }
                            }
                            viewModel.refresh()
                        })
                    }

                    
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onCrashClick = { model -> navController.navigate("detail/${model.id}") },
                            onSettingsClick = { navController.navigate("settings") }
                        )
                    }

                    
                    composable("settings") {
    SettingsScreen(
        onBackClick = { 
            navController.popBackStack()
            viewModel.refresh()
        },
        onAboutClick = {
            navController.navigate("about")
        }
    )
}

                    composable("about") {
    AboutScreen(
        onBackClick = {
            navController.popBackStack()
        }
    )
}
                    composable(
                        route = "detail/{crashId}",
                        arguments = listOf(navArgument("crashId") { type = NavType.LongType })
                    ) { entry ->
                        val id = entry.arguments?.getLong("crashId") ?: 0L
                        viewModel.getCrashById(id)?.let {
                            CrashDetailScreen(
                                crash = it.originalEvent,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
