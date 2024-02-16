@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.familybudget

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.faigenbloom.familybudget.common.CALENDAR_END_DATE_ARG
import com.faigenbloom.familybudget.common.CALENDAR_START_DATE_ARG
import com.faigenbloom.familybudget.common.CATEGORY_PHOTO
import com.faigenbloom.familybudget.common.DETAILS_LIST_ARG
import com.faigenbloom.familybudget.common.FloatingActionMenu
import com.faigenbloom.familybudget.common.FloatingMenuState
import com.faigenbloom.familybudget.common.GalleryPhotoContract
import com.faigenbloom.familybudget.common.GalleryRequest
import com.faigenbloom.familybudget.common.ID_ARG
import com.faigenbloom.familybudget.common.PHOTO_KEY
import com.faigenbloom.familybudget.common.PHOTO_REASON_ARG
import com.faigenbloom.familybudget.common.QR_KEY
import com.faigenbloom.familybudget.common.SPENDING_PHOTO
import com.faigenbloom.familybudget.common.toJson
import com.faigenbloom.familybudget.ui.budget.budgetPage
import com.faigenbloom.familybudget.ui.calendar.CalendarRoute
import com.faigenbloom.familybudget.ui.calendar.calendarDialog
import com.faigenbloom.familybudget.ui.camera.CameraRoute
import com.faigenbloom.familybudget.ui.camera.cameraPage
import com.faigenbloom.familybudget.ui.chooser.ImageSourceChooserRoute
import com.faigenbloom.familybudget.ui.chooser.imageSourceChooserDialog
import com.faigenbloom.familybudget.ui.family.FamilyRoute
import com.faigenbloom.familybudget.ui.family.familyPage
import com.faigenbloom.familybudget.ui.login.LoginRoute
import com.faigenbloom.familybudget.ui.login.loginPage
import com.faigenbloom.familybudget.ui.onboarding.OnboardingRoute
import com.faigenbloom.familybudget.ui.onboarding.onboardingPage
import com.faigenbloom.familybudget.ui.register.RegisterRoute
import com.faigenbloom.familybudget.ui.register.registerPage
import com.faigenbloom.familybudget.ui.settings.settingsPage
import com.faigenbloom.familybudget.ui.spendings.detail.DetailDialogRoute
import com.faigenbloom.familybudget.ui.spendings.detail.detailDialog
import com.faigenbloom.familybudget.ui.spendings.edit.SpendingEditRoute
import com.faigenbloom.familybudget.ui.spendings.edit.spendingEditPage
import com.faigenbloom.familybudget.ui.spendings.list.SpendingsListPage
import com.faigenbloom.familybudget.ui.spendings.list.spendingsListPage
import com.faigenbloom.familybudget.ui.spendings.show.SpendingShowRoute
import com.faigenbloom.familybudget.ui.spendings.show.spendingShowPage
import com.faigenbloom.familybudget.ui.statistics.statisticsPage
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var mainNavController: NavHostController
    private lateinit var scanQrCodeLauncher: ActivityResultLauncher<Nothing?>
    private lateinit var galleryLauncher: ActivityResultLauncher<GalleryRequest>
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
        galleryLauncher = registerForActivityResult(GalleryPhotoContract(this)) { galleryResponse ->
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

            var floatingMenuState by remember {
                mutableStateOf<FloatingMenuState?>(null)
            }
            var selectedBottomNavigationIndex by rememberSaveable { mutableStateOf(0) }
            mainNavController = rememberNavController()
            FamillySpandingsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        bottomBar = {
                            if (withBottomNavigation) {
                                BottomNavigationBar(
                                    selectedIndex = selectedBottomNavigationIndex,
                                    onDestinationChanged = { index, destination ->
                                        selectedBottomNavigationIndex = index
                                        mainNavController.navigate(destination)
                                    },
                                )
                            }
                        },
                        floatingActionButton = {
                            FloatingActionMenu(
                                state = floatingMenuState,
                            )
                        },
                    ) { padding ->
                        NavHost(
                            navController = mainNavController,
                            startDestination = OnboardingRoute(),
                        ) {
                            onboardingPage(
                                onLogin = {
                                    mainNavController.navigate(LoginRoute())
                                },
                                onRegister = {
                                    mainNavController.navigate(RegisterRoute())
                                },
                                onLoggedIn = {
                                    mainNavController.navigate(SpendingsListPage())
                                },
                            )
                            loginPage(
                                onLoggedIn = {
                                    mainNavController.navigate(SpendingsListPage())
                                },
                                onBack = {
                                    mainNavController.popBackStack()
                                },
                            )
                            registerPage(
                                onLoggedIn = {
                                    mainNavController.navigate(SpendingsListPage())
                                },
                                onBack = {
                                    mainNavController.popBackStack()
                                },
                            )
                            spendingsListPage(
                                padding = padding,
                                options = {
                                        showNavigation: Boolean,
                                        index: Int,
                                    ->
                                    withBottomNavigation = showNavigation
                                    selectedBottomNavigationIndex = index

                                },
                                menuCallback = { floatingMenuState = it },
                                onCalendarRequested = { from, to ->
                                    mainNavController.navigate(
                                        CalendarRoute(startDate = from, endDate = to),
                                    )
                                },
                                onOpenSpending = {
                                    mainNavController.navigate(
                                        SpendingShowRoute(it),
                                    )
                                },
                            )
                            spendingEditPage(
                                options = { bottomNavigation, menuState ->
                                    withBottomNavigation = bottomNavigation
                                    floatingMenuState = menuState
                                },
                                onNext = { spendingId ->
                                    mainNavController.navigate(
                                        SpendingShowRoute(spendingId),
                                    )
                                },
                                onBack = ::PopSpendings,
                                onPhotoRequest = { spendingId ->
                                    if (requestCameraPermission()) {
                                        mainNavController.navigate(
                                            ImageSourceChooserRoute(
                                                SPENDING_PHOTO,
                                                spendingId,
                                            ),
                                        )
                                    }
                                },
                                onCategoryPhotoRequest = { categoryId ->
                                    if (requestCameraPermission()) {
                                        mainNavController.navigate(
                                            ImageSourceChooserRoute(
                                                CATEGORY_PHOTO,
                                                categoryId,
                                            ),
                                        )
                                    }
                                },
                                onCalendarOpened = {
                                    mainNavController.navigate(
                                        CalendarRoute(startDate = it),
                                    )
                                },
                                onSpendingDialogRequest = {
                                    mainNavController.navigate(
                                        DetailDialogRoute(it),
                                    )
                                },
                            )
                            spendingShowPage(
                                bottomNavigationOptions = { withBottomNavigation = it },
                                options = { floatingMenuState = it },
                                onEditClicked = {
                                    mainNavController.navigate(
                                        SpendingEditRoute(it),
                                    )
                                },
                                onBack = {
                                    PopSpendings()
                                },
                            )
                            statisticsPage(
                                bottomNavigationOptions = {
                                        showNavigation: Boolean,
                                        index: Int,
                                    ->
                                    withBottomNavigation = showNavigation
                                    selectedBottomNavigationIndex = index
                                },
                                options = { menuState ->
                                    floatingMenuState = menuState
                                },
                                onCalendarRequested = { from, to ->
                                    mainNavController.navigate(
                                        CalendarRoute(startDate = from, endDate = to),
                                    )
                                },
                            )
                            budgetPage(
                                bottomNavigationOptions = {
                                        showNavigation: Boolean,
                                        index: Int,
                                    ->
                                    withBottomNavigation = showNavigation
                                    selectedBottomNavigationIndex = index
                                },
                                options = { menuState ->
                                    floatingMenuState = menuState
                                },
                                toSpendings = { from, to ->
                                    //TODO: MAYBE add this date
                                    mainNavController.navigate(SpendingEditRoute())
                                },
                            )
                            settingsPage(
                                bottomNavigationOptions = {
                                        showNavigation: Boolean,
                                        index: Int,
                                    ->
                                    withBottomNavigation = showNavigation
                                    selectedBottomNavigationIndex = index
                                    floatingMenuState = null
                                },
                                options = { floatingMenuState = it },
                                onFamilyPageClicked = {
                                    mainNavController.navigate(FamilyRoute())
                                },
                            )
                            familyPage(
                                bottomNavigationOptions = {
                                        showNavigation: Boolean,
                                    ->
                                    withBottomNavigation = showNavigation
                                    floatingMenuState = null
                                },
                                options = { floatingMenuState = it },
                                onQRScanRequested = {
                                    scanQrCodeLauncher.launch(null)
                                },
                                onBack = {
                                    mainNavController.popBackStack()
                                },
                            )
                            cameraPage(
                                bottomNavigationOptions = {
                                    withBottomNavigation = it
                                    floatingMenuState = null
                                },
                                outputDirectory = getOutputDirectory(),
                                onImageCaptured = { uri, photoReason, id ->
                                    handleImageCapture(
                                        uri = uri,
                                        photoReason = photoReason,
                                        id = id,
                                        mainNavController,
                                    )
                                },
                            )
                            imageSourceChooserDialog(
                                onDismissRequest = {
                                    mainNavController.popBackStack()
                                },
                                onGalleryChosen = { reason, id ->
                                    galleryLauncher.launch(
                                        GalleryRequest(
                                            id = id,
                                            reason = reason,
                                        ),
                                    )
                                },
                                onCameraChosen = { reason, id ->
                                    if (requestCameraPermission()) {
                                        mainNavController.navigate(
                                            CameraRoute(
                                                reason = reason,
                                                id = id,
                                            ),
                                        )
                                    }
                                },
                            )
                            calendarDialog(
                                onDatePicked = { startDate, endDate ->
                                    mainNavController.popBack(
                                        hashMapOf(
                                            CALENDAR_START_DATE_ARG to startDate,
                                            CALENDAR_END_DATE_ARG to endDate,
                                        ),
                                    )
                                },
                            )
                            detailDialog(
                                onSave = { updateDetails ->
                                    mainNavController.popBack(
                                        hashMapOf(
                                            DETAILS_LIST_ARG to updateDetails.toJson(),
                                        ),
                                    )
                                },
                                onBack = { mainNavController.popBackStack() },
                            )
                        }
                    }
                }
            }
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun PopSpendings() {
        mainNavController.popBackStack()

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
            ) == PackageManager.PERMISSION_GRANTED -> {
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
        previousBackStackEntry?.savedStateHandle?.apply {
            data.forEach {
                set(it.key, it.value)
            }
        }
        popBackStack()
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
