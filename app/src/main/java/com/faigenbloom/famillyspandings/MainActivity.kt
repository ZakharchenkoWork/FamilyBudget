@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.faigenbloom.famillyspandings.budget.BudgetPage
import com.faigenbloom.famillyspandings.budget.BudgetPageViewModel
import com.faigenbloom.famillyspandings.comon.CameraScreen
import com.faigenbloom.famillyspandings.comon.Destination
import com.faigenbloom.famillyspandings.comon.PHOTO_KEY
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.family.FamilyPage
import com.faigenbloom.famillyspandings.family.FamilyPageViewModel
import com.faigenbloom.famillyspandings.login.LoginPage
import com.faigenbloom.famillyspandings.login.LoginPageViewModel
import com.faigenbloom.famillyspandings.onboarding.OnboardingPage
import com.faigenbloom.famillyspandings.register.RegisterPage
import com.faigenbloom.famillyspandings.register.RegisterPageViewModel
import com.faigenbloom.famillyspandings.settings.SettingsPage
import com.faigenbloom.famillyspandings.settings.SettingsPageViewModel
import com.faigenbloom.famillyspandings.spandings.SpandingsPage
import com.faigenbloom.famillyspandings.spandings.SpendingsPageViewModel
import com.faigenbloom.famillyspandings.spandings.edit.SpendingEditPage
import com.faigenbloom.famillyspandings.spandings.edit.SpendingEditViewModel
import com.faigenbloom.famillyspandings.spandings.show.SpendingShowPage
import com.faigenbloom.famillyspandings.spandings.show.SpendingShowViewModel
import com.faigenbloom.famillyspandings.statistics.StatisticsPage
import com.faigenbloom.famillyspandings.statistics.StatisticsPageViewModel
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val isLoggedIn: Boolean = true

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var withBottomNavigation by remember {
                mutableStateOf(false)
            }
            val mainNavController = rememberNavController()
            FamillySpandingsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        bottomBar = {
                            if (withBottomNavigation) {
                                BottomNavigationBar(
                                    onDestinationChanged = {
                                        mainNavController.navigate(it.route)
                                    },
                                )
                            }
                        },
                    ) { padding ->
                        NavHost(
                            navController = mainNavController,
                            startDestination = if (!isLoggedIn) {
                                Destination.Onboarding.route
                            } else {
                                Destination.SpendingsPage.route
                            },
                        ) {
                            composable(
                                route = Destination.Onboarding.route,
                            ) {
                                withBottomNavigation = false
                                OnboardingPage(
                                    onLogin = {
                                        mainNavController.navigate(Destination.Login.route)
                                    },
                                    onRegister = {
                                        mainNavController.navigate(Destination.Register.route)
                                    },
                                )
                            }

                            composable(
                                route = Destination.Login.route,
                            ) {
                                withBottomNavigation = false
                                val loginPageViewModel = koinViewModel<LoginPageViewModel>()
                                loginPageViewModel.onLoggedIn = {
                                    mainNavController.navigate(Destination.SpendingsPage.route)
                                }
                                val state by loginPageViewModel
                                    .loginStateFlow
                                    .collectAsState()
                                LoginPage(state)
                            }
                            composable(
                                route = Destination.Register.route,
                            ) {
                                withBottomNavigation = false
                                val registerPageViewModel = koinViewModel<RegisterPageViewModel>()
                                registerPageViewModel.onLoggedIn = {
                                    mainNavController.navigate(Destination.SpendingsPage.route)
                                }
                                val state by registerPageViewModel
                                    .loginStateFlow
                                    .collectAsState()
                                RegisterPage(state)
                            }

                            composable(
                                route = Destination.SpendingsPage.route,
                            ) {
                                withBottomNavigation = true
                                val state by koinViewModel<SpendingsPageViewModel>()
                                    .spendingsStateFlow
                                    .collectAsState()
                                SpandingsPage(
                                    modifier = Modifier.padding(
                                        bottom = padding.calculateBottomPadding(),
                                    ),
                                    state = state,
                                    onOpenSpending = {
                                        mainNavController.navigate(
                                            Destination.SpendingShowPage.withId(
                                                it,
                                            ),
                                        )
                                    },
                                )
                            }
                            composable(
                                route = Destination.SpendingEditPage.route,
                            ) {
                                withBottomNavigation = false

                                val state by koinViewModel<SpendingEditViewModel>()
                                    .spendingEditStateFlow
                                    .collectAsState()

                                val photoUri = mainNavController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.getStateFlow<Uri?>(PHOTO_KEY, null)?.collectAsState()
                                state.onPhotoUriChanged(photoUri?.value)
                                SpendingEditPage(
                                    state = state,
                                    onPhotoRequest = {
                                        if (requestCameraPermission()) {
                                            mainNavController.navigate(Destination.Camera.route)
                                        }
                                    },
                                )
                            }
                            composable(
                                route = Destination.Camera.route,
                            ) {
                                withBottomNavigation = false
                                CameraScreen(
                                    outputDirectory = outputDirectory,
                                    executor = cameraExecutor,
                                    onImageCaptured = { handleImageCapture(it, mainNavController) },
                                    onError = { Log.e("kilo", "View error:", it) },
                                )
                            }
                            composable(
                                route = Destination.SpendingShowPage.route,
                                arguments = listOf(
                                    navArgument(SPENDING_ID_ARG) {
                                        type = NavType.StringType
                                    },
                                ),
                            ) {
                                withBottomNavigation = false

                                val state by koinViewModel<SpendingShowViewModel>()
                                    .spendingsStateFlow
                                    .collectAsState()

                                SpendingShowPage(
                                    state = state,
                                    onEditClicked = {
                                        mainNavController.navigate(
                                            Destination.SpendingEditPage.withId(
                                                it,
                                            ),
                                        )
                                    },
                                )
                            }
                            composable(
                                route = Destination.StatisticsPage.route,
                            ) {
                                withBottomNavigation = true

                                val state by koinViewModel<StatisticsPageViewModel>()
                                    .statisicsStateFlow
                                    .collectAsState()

                                StatisticsPage(state)
                            }
                            composable(
                                route = Destination.BudgetPage.route,
                            ) {
                                withBottomNavigation = true

                                val state by koinViewModel<BudgetPageViewModel>()
                                    .budgetStateFlow
                                    .collectAsState()

                                BudgetPage(
                                    state = state,
                                    onAddSpendingClicked = {
                                        mainNavController.navigate(Destination.SpendingEditPage.route)
                                    },
                                    onAddPlannedSpendingClicked = {
                                        mainNavController.navigate(Destination.SpendingEditPage.route)
                                    },
                                )
                            }
                            composable(
                                route = Destination.SettingsPage.route,
                            ) {
                                withBottomNavigation = true

                                val state by koinViewModel<SettingsPageViewModel>()
                                    .budgetStateFlow
                                    .collectAsState()

                                SettingsPage(
                                    state = state,
                                    onFamilyPageClicked = {
                                        mainNavController.navigate(Destination.FamilyPage.route)
                                    },
                                )
                            }
                            composable(
                                route = Destination.FamilyPage.route,
                            ) {
                                withBottomNavigation = false

                                val state by koinViewModel<FamilyPageViewModel>()
                                    .familyStateFlow
                                    .collectAsState()
                                FamilyPage(
                                    state = state,
                                )
                            }
                        }
                    }
                }
            }
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            requestCameraPermission()
        } else {
            Log.i("Spendings", "Permission NOT Granted")
        }
    }

    private fun requestCameraPermission(): Boolean {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA,
            )
                == PackageManager.PERMISSION_GRANTED -> {
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA,
            ) -> return true

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
        return false
    }

    private fun handleImageCapture(uri: Uri, mainNavController: NavController) {
        lifecycleScope.launch {
            mainNavController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(PHOTO_KEY, uri)
            mainNavController.popBackStack(Destination.SpendingEditPage.route, false)
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
