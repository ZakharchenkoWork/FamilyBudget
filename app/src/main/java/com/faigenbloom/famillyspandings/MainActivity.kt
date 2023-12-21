@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.faigenbloom.famillyspandings.budget.BudgetPage
import com.faigenbloom.famillyspandings.budget.BudgetPageViewModel
import com.faigenbloom.famillyspandings.comon.CATEGORY_PHOTO
import com.faigenbloom.famillyspandings.comon.CameraScreen
import com.faigenbloom.famillyspandings.comon.Destination
import com.faigenbloom.famillyspandings.comon.GalleryPhotoContract
import com.faigenbloom.famillyspandings.comon.GalleryRequest
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.comon.PHOTO_KEY
import com.faigenbloom.famillyspandings.comon.PHOTO_REASON_ARG
import com.faigenbloom.famillyspandings.comon.PhotoChooser
import com.faigenbloom.famillyspandings.comon.QR_KEY
import com.faigenbloom.famillyspandings.comon.SPENDING_ID_ARG
import com.faigenbloom.famillyspandings.comon.SPENDING_PHOTO
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
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var mainNavController: NavHostController
    private lateinit var scanQrCodeLauncher: ActivityResultLauncher<Nothing?>
    private lateinit var galleryLauncher: ActivityResultLauncher<GalleryRequest>
    private val isLoggedIn: Boolean = true
    override fun onStart() {
        super.onStart()
        scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
            when (result) {
                is QRResult.QRSuccess -> {
                    result.content.rawValue?.let {
                        handleQRCapture(it, mainNavController)
                    }
                }

                else -> {
                }
            }
        }
        galleryLauncher =
            registerForActivityResult(GalleryPhotoContract()) { galleryResponse ->
                galleryResponse?.uri?.let {
                    handleImageCapture(
                        uri = it,
                        photoReason = galleryResponse.reason,
                        id = galleryResponse.id,
                        mainNavController = mainNavController,
                    )
                }
            }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var withBottomNavigation by remember {
                mutableStateOf(false)
            }
            mainNavController = rememberNavController()

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
                            val savedStateHandle = mainNavController
                                .currentBackStackEntry
                                ?.savedStateHandle
                            composable(
                                route = Destination.SpendingEditPage.route,
                            ) {
                                withBottomNavigation = false

                                val state by koinViewModel<SpendingEditViewModel>()
                                    .spendingEditStateFlow
                                    .collectAsState()
                                savedStateHandle
                                    ?.getStateFlow<String?>(PHOTO_REASON_ARG, null)
                                    ?.collectAsState()?.value?.let { reason ->
                                        val id: String? = savedStateHandle[ID_ARG]
                                        when (reason) {
                                            SPENDING_PHOTO -> {
                                                if (id == state.spendingId) {
                                                    state.onPhotoUriChanged(
                                                        savedStateHandle[PHOTO_KEY],
                                                    )
                                                } else {
                                                }
                                            }

                                            CATEGORY_PHOTO -> {
                                                val uri: Uri? = savedStateHandle[PHOTO_KEY]
                                                uri?.let {
                                                    state.categoryState.onCategoryPhotoUriChanged(
                                                        id ?: "",
                                                        it,
                                                    )
                                                }
                                            }

                                            else -> {}
                                        }
                                    }
                                SpendingEditPage(
                                    state = state,
                                    onPhotoRequest = { spendingId ->
                                        if (requestCameraPermission()) {
                                            mainNavController.navigate(
                                                Destination.PhotoChooserDialog.withReason(
                                                    SPENDING_PHOTO,
                                                    spendingId,
                                                ),
                                            )
                                        }
                                    },
                                    onCategoryPhotoRequest = { categoryId ->
                                        if (requestCameraPermission()) {
                                            mainNavController.navigate(
                                                Destination.PhotoChooserDialog.withReason(
                                                    CATEGORY_PHOTO,
                                                    categoryId,
                                                ),
                                            )
                                        }
                                    },
                                )
                            }
                            composable(
                                route = Destination.Camera.route,
                                arguments = listOf(
                                    navArgument(PHOTO_REASON_ARG) {
                                        type = NavType.StringType
                                    },
                                    navArgument(ID_ARG) {
                                        type = NavType.StringType
                                    },
                                ),
                            ) { backStackEntry ->
                                val reason = backStackEntry.arguments?.getString(PHOTO_REASON_ARG)
                                val id = backStackEntry.arguments?.getString(ID_ARG)
                                withBottomNavigation = false
                                CameraScreen(
                                    outputDirectory = outputDirectory,
                                    executor = cameraExecutor,
                                    onImageCaptured = {
                                        handleImageCapture(
                                            uri = it,
                                            photoReason = reason,
                                            id = id,
                                            mainNavController,
                                        )
                                    },
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
                                val qrCodeScanned = savedStateHandle?.get<String>(QR_KEY)
                                state.onQrScanned(qrCodeScanned)
                                FamilyPage(
                                    state = state,
                                    onQRScanRequested = {
                                        scanQrCodeLauncher.launch(null)
                                    },
                                )
                            }
                            dialog(
                                route = Destination.PhotoChooserDialog.route,
                                dialogProperties = DialogProperties(
                                    dismissOnBackPress = true,
                                    dismissOnClickOutside = true,
                                ),
                                arguments = listOf(
                                    navArgument(PHOTO_REASON_ARG) {
                                        type = NavType.StringType
                                    },
                                    navArgument(ID_ARG) {
                                        type = NavType.StringType
                                    },
                                ),
                            ) { backStackEntry ->
                                val reason = backStackEntry.arguments?.getString(PHOTO_REASON_ARG)
                                val id = backStackEntry.arguments?.getString(ID_ARG)

                                PhotoChooser(
                                    onDismissRequest = {
                                        mainNavController.popBackStack()
                                    },
                                    onGalleryChoosen = {
                                        galleryLauncher.launch(
                                            GalleryRequest(
                                                id = id,
                                                reason = reason,
                                            ),
                                        )
                                    },
                                    onCameraChoosen = {
                                        if (requestCameraPermission()) {
                                            mainNavController.navigate(
                                                Destination.Camera
                                                    .withReason(
                                                        reason = reason,
                                                        id = id,
                                                    ),
                                            )
                                        }
                                    },
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

    private fun requestGallery() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        )
        startActivityForResult(pickPhoto, 1) // one can be replaced with any action code
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

    private fun handleQRCapture(text: String, mainNavController: NavController) {
        lifecycleScope.launch {
            mainNavController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(QR_KEY, text)
            mainNavController.popBackStack(Destination.FamilyPage.route, false)
        }
    }

    private fun handleImageCapture(
        uri: Uri,
        photoReason: String?,
        id: String?,
        mainNavController: NavController,
    ) {
        lifecycleScope.launch {
            mainNavController.previousBackStackEntry
                ?.savedStateHandle?.apply {
                    set(PHOTO_REASON_ARG, photoReason)
                    set(PHOTO_KEY, uri)
                    set(ID_ARG, id)
                }
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
