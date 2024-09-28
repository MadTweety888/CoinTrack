package com.example.cointrack.ui.screens.main.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cointrack.R
import com.example.cointrack.domain.models.UserData
import com.example.cointrack.ui.activities.AccountActivity
import com.example.cointrack.ui.activities.MainActivityViewModel
import com.example.cointrack.ui.screens.main.profile.ProfileScreenViewModel.Events.NavigateToSplashScreen
import com.example.cointrack.ui.theme.Grey50
import com.example.cointrack.ui.theme.Grey90
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.ui.util.components.BoxWithDiagonalBackgroundPattern
import com.example.cointrack.ui.util.components.dialogs.DestroyingActionDialog
import com.example.cointrack.ui.util.primary.PrimaryButton
import com.example.cointrack.ui.util.primary.PrimaryErrorScreen
import com.example.cointrack.ui.util.primary.PrimaryTextField
import com.example.cointrack.util.extentions.findActivity
import com.example.cointrack.util.extentions.shimmerLoadingAnimation
import com.skydoves.landscapist.glide.GlideImage

private const val PHOTO_SIZE = 120

@Composable
fun ProfileScreen(
    navController: NavHostController,
    mainViewModel: MainActivityViewModel
) {

    val viewModel = hiltViewModel<ProfileScreenViewModel>()


    ProfileScreenView(viewModel, mainViewModel)

    IsLoadingState(viewModel)

    IsErrorState(viewModel)

    LogOutDialogState(viewModel, mainViewModel)

    DeleteAccountDialogState(viewModel, mainViewModel)

    EventsHandler(viewModel)
}

@Composable
private fun ProfileScreenView(
    viewModel: ProfileScreenViewModel,
    mainViewModel: MainActivityViewModel
) {

    val focusManager = LocalFocusManager.current
    
    val userData by remember { viewModel.editingUserData }

    val selectImageLauncher = createSelectImageLauncher { viewModel.onPhotoPicked(it) }

    BoxWithDiagonalBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            ProfilePhotoSection(
                photoUrl = userData?.photoUrl ?: "",
                onEditPhotoClicked = { selectImageLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            ProfileDetailsSection(
                viewModel = viewModel,
                userData = userData
            )

            Spacer(modifier = Modifier.weight(1f))

            ProfileButtonSection(viewModel, mainViewModel)

            Spacer(
                modifier = Modifier
                    .height(ComponentSizes.bottomNavBarHeight.dp + MaterialTheme.spacing.large)
            )
        }
    }
}

@Composable
private fun ProfilePhotoSection(
    photoUrl: String,
    onEditPhotoClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        GlideImage(
            modifier = Modifier
                .size(PHOTO_SIZE.dp)
                .clip(CircleShape)
                .clickable { onEditPhotoClicked() },
            imageModel = { photoUrl },
            loading = {

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.primary
                )
            },
            failure = {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Grey90),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        modifier = Modifier.scale(3f),
                        painter = painterResource(id = R.drawable.default_profile_icon),
                        tint = Grey50,
                        contentDescription = null
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onEditPhotoClicked() }
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.extraSmall
                )
        ) {

            Text(
                text = "Edit photo",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun ProfileDetailsSection(
    viewModel: ProfileScreenViewModel,
    userData: UserData?
) {

    val focusManager = LocalFocusManager.current

    val isSaveButtonVisible by remember { viewModel.isSaveButtonVisible }

    Column {

        PrimaryTextField(
            modifier = Modifier.height(48.dp),
            enabled = false,
            text = userData?.email ?: "",
            onTextChanged = { /* NO ACTION */ },
            placeholder = "Email"
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryTextField(
            text = userData?.name ?: "",
            onTextChanged = { viewModel.onNameChanged(it) },
            placeholder = "Name",
            trailingIcon = Icons.Default.Close,
            onTrailingIconClick = { viewModel.onNameChanged("") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryTextField(
            text = userData?.surname ?: "",
            onTextChanged = { viewModel.onSurnameChanged(it) },
            placeholder = "Surname",
            trailingIcon = Icons.Default.Close,
            onTrailingIconClick = { viewModel.onSurnameChanged("") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        AnimatedVisibility(
            visible = isSaveButtonVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            PrimaryButton(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.small)
                    .fillMaxWidth(),
                text = "Save",
                onClick = viewModel::onSaveButtonClicked
            )
        }
    }
}

@Composable
private fun ProfileButtonSection(
    viewModel: ProfileScreenViewModel,
    mainViewModel: MainActivityViewModel
) {

    Column {

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {

                    mainViewModel.hideBottomNavBar()
                    viewModel.onLogOutButtonClicked()
                }
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.extraSmall
                )
        ) {

            Text(
                text = "Log Out",
                style = MaterialTheme.typography.h6,
                color = RedError
            )
        }
        
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {

                    mainViewModel.hideBottomNavBar()
                    viewModel.onDeleteAccountButtonClicked()
                }
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.extraSmall
                )
        ) {

            Text(
                text = "Delete Account",
                style = MaterialTheme.typography.h6,
                color = RedError
            )
        }
    }
}

@Composable
private fun IsLoadingState(
    viewModel: ProfileScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .pointerInput(Unit) { detectTapGestures(onTap = { /* NO ACTION */ }) }
        ) {

            BoxWithDiagonalBackgroundPattern {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                ) {

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(PHOTO_SIZE.dp)
                                .clip(CircleShape)
                                .shimmerLoadingAnimation()
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .width(128.dp)
                                .clip(CircleShape)
                                .shimmerLoadingAnimation()
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    Column {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerLoadingAnimation()
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerLoadingAnimation()
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerLoadingAnimation()
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column {

                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .width(72.dp)
                                .clip(CircleShape)
                                .shimmerLoadingAnimation()
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .width(138.dp)
                                .clip(CircleShape)
                                .shimmerLoadingAnimation()
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(ComponentSizes.bottomNavBarHeight.dp + MaterialTheme.spacing.large)
                    )
                }
            }
        }
    }
}

@Composable
private fun IsErrorState(
    viewModel: ProfileScreenViewModel
) {

    val isError by remember { viewModel.isError }

    isError?.let { error ->

        PrimaryErrorScreen(errorText = error)
    }
}

@Composable
private fun LogOutDialogState(
    viewModel: ProfileScreenViewModel,
    mainViewModel: MainActivityViewModel
) {

    val isLogOutDialogVisible by remember { viewModel.isLogOutDialogVisible }

    DestroyingActionDialog(
        isDisplayed = isLogOutDialogVisible,
        title = "Log Out?",
        firstText = "Are you sure you want to log out?",
        secondText = "",
        buttonText = "Log Out",
        onAction = {

            mainViewModel.showBottomNavBar()
            viewModel.onLogOutDialogConfirmed()
        },
        onCancel = {

            mainViewModel.showBottomNavBar()
            viewModel.onLogOutDialogDismissed()
        }
    )
}

@Composable
private fun DeleteAccountDialogState(
    viewModel: ProfileScreenViewModel,
    mainViewModel: MainActivityViewModel
) {

    val isDeleteAccountDialogVisible by remember { viewModel.isDeleteAccountDialogVisible }

    DestroyingActionDialog(
        isDisplayed = isDeleteAccountDialogVisible,
        onAction = {

            mainViewModel.showBottomNavBar()
            viewModel.onDeleteAccountDialogConfirmed()
        },
        onCancel = {

            mainViewModel.showBottomNavBar()
            viewModel.onDeleteAccountDialogDismissed()
        }
    )
}

@Composable
private fun EventsHandler(
    viewModel: ProfileScreenViewModel
) {

    val context = LocalContext.current

    val event by viewModel.events.collectAsState(initial = null)

    when (event) {

        is NavigateToSplashScreen -> {
            context.startActivity(Intent(context, AccountActivity::class.java))
            context.findActivity()?.finish()
        }
        else -> { /* NO ACTION */ }
    }
}

@Composable
private fun createSelectImageLauncher(
    onPhotoPicked: (uri: Uri?) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri ->

    onPhotoPicked(uri)
}