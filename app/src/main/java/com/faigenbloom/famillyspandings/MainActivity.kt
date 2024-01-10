@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
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
import com.faigenbloom.famillyspandings.comon.CALENDAR_START_DATE_ARG
import com.faigenbloom.famillyspandings.comon.CATEGORY_PHOTO
import com.faigenbloom.famillyspandings.comon.Calendar
import com.faigenbloom.famillyspandings.comon.CameraScreen
import com.faigenbloom.famillyspandings.comon.DATE
import com.faigenbloom.famillyspandings.comon.Destination
import com.faigenbloom.famillyspandings.comon.GalleryPhotoContract
import com.faigenbloom.famillyspandings.comon.GalleryRequest
import com.faigenbloom.famillyspandings.comon.ID_ARG
import com.faigenbloom.famillyspandings.comon.OPTIONAL_ID_ARG
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
import com.faigenbloom.famillyspandings.spandings.SpendingsPage
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
            registerForActivityResult(GalleryPhotoContract(this)) { galleryResponse ->
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
            val snackBarBuilder = SnackBarBuilder(
                scope = lifecycleScope,
                snackbarHostState = remember { SnackbarHostState() },
            )
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
                        snackbarHost = {
                            SnackbarHost(hostState = snackBarBuilder.snackbarHostState)
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
                                val viewModel = koinViewModel<SpendingsPageViewModel>()
                                val state by viewModel
                                    .spendingsStateFlow
                                    .collectAsState()
                                viewModel.reloadData()
                                SpendingsPage(
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
                                arguments = listOf(
                                    navArgument(SPENDING_ID_ARG) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                ),
                            ) { backStack ->
                                withBottomNavigation = false
                                val viewModel = koinViewModel<SpendingEditViewModel>()
                                viewModel.onNext = { spendingId ->
                                    mainNavController.navigate(
                                        Destination.SpendingShowPage.withId(spendingId),
                                    )
                                    snackBarBuilder.show(getString(R.string.message_saved))
                                }

                                val state by viewModel
                                    .spendingEditStateFlow
                                    .collectAsState()
                                backStack.getPoppedArgument(DATE, "")?.let { calendarDate ->
                                    state.onDateChanged(calendarDate)
                                }
                                backStack.getPoppedArgument<String>(PHOTO_REASON_ARG)
                                    ?.let { reason ->

                                        val id: String? = backStack.getPoppedArgument(ID_ARG)
                                        when (reason) {
                                            SPENDING_PHOTO -> {
                                                if (id == state.spendingId) {
                                                    state.onPhotoUriChanged(
                                                        backStack.getPoppedArgument(PHOTO_KEY),
                                                    )
                                                } else {
                                                }
                                            }

                                            CATEGORY_PHOTO -> {
                                                val uri: Uri? =
                                                    backStack.getPoppedArgument(PHOTO_KEY)
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
                                    onCalendarOpened = {
                                        mainNavController.navigate(
                                            Destination.CalendarDialog.withDate(it),
                                        )
                                    },
                                )
                            }

                            composable(
                                route = Destination.SpendingShowPage.route,
                                arguments = listOf(
                                    navArgument(ID_ARG) {
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
                                        mainNavController.navigate(Destination.SpendingEditPage.withoutId())
                                    },
                                    onAddPlannedSpendingClicked = {
                                        mainNavController.navigate(Destination.SpendingEditPage.withoutId())
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
                                val qrCodeScanned = it.savedStateHandle.get<String>(QR_KEY)
                                state.onQrScanned(qrCodeScanned)
                                FamilyPage(
                                    state = state,
                                    onQRScanRequested = {
                                        scanQrCodeLauncher.launch(null)
                                    },
                                )
                            }
                            composable(
                                route = Destination.Camera.route,
                                arguments = listOf(
                                    navArgument(PHOTO_REASON_ARG) {
                                        type = NavType.StringType
                                    },
                                    navArgument(OPTIONAL_ID_ARG) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                ),
                            ) { backStackEntry ->
                                val reason = backStackEntry.arguments?.getString(PHOTO_REASON_ARG)
                                val id = backStackEntry.arguments?.getString(OPTIONAL_ID_ARG)
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
                                    onError = { Log.e("Camera", "View error:", it) },
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
                                    navArgument(OPTIONAL_ID_ARG) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                ),
                            ) { backStackEntry ->
                                val reason = backStackEntry.arguments?.getString(PHOTO_REASON_ARG)
                                val id = backStackEntry.arguments?.getString(OPTIONAL_ID_ARG)

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

                            dialog(
                                route = Destination.CalendarDialog.route,
                                dialogProperties = DialogProperties(
                                    usePlatformDefaultWidth = false,
                                    dismissOnBackPress = true,
                                    dismissOnClickOutside = true,
                                ),
                                arguments = listOf(
                                    navArgument(CALENDAR_START_DATE_ARG) {
                                        type = NavType.StringType
                                    },
                                ),
                            ) { backStackEntry ->
                                val startDate =
                                    backStackEntry.arguments?.getString(CALENDAR_START_DATE_ARG)
                                        ?: ""

                                Calendar(
                                    startDate = startDate,
                                    onDatePicked = { date ->
                                        mainNavController.popBack(
                                            hashMapOf(
                                                DATE to date,
                                            ),
                                        )
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
            mainNavController.popBack(
                hashMapOf(
                    QR_KEY to text,
                ),
            )
        }
    }

    private fun handleImageCapture(
        uri: Uri,
        photoReason: String?,
        id: String?,
        mainNavController: NavController,
    ) {
        lifecycleScope.launch {
            mainNavController.popBack(
                hashMapOf(
                    PHOTO_REASON_ARG to photoReason,
                    PHOTO_KEY to uri,
                    ID_ARG to id,
                ),
            )
        }
    }

    fun NavController.popBack(data: HashMap<String, Any?>) {
        previousBackStackEntry
            ?.savedStateHandle?.apply {
                data.forEach {
                    set(it.key, it.value)
                }
            }
        mainNavController.popBackStack()
    }

    @Composable
    fun <T> NavBackStackEntry.getPoppedArgument(argumentKey: String, initial: T? = null) =
        this.savedStateHandle
            .getStateFlow(argumentKey, initial)
            .collectAsState().value

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

    class SnackBarBuilder(
        val scope: LifecycleCoroutineScope,
        val snackbarHostState: SnackbarHostState,
    ) {
        fun show(message: String) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                )
            }
        }
    }
}
