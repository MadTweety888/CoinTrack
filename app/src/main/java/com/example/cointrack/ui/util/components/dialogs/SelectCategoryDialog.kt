package com.example.cointrack.ui.util.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.R
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Dark
import com.example.cointrack.ui.theme.Grey85
import com.example.cointrack.ui.theme.RedError
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.ui.util.primary.PrimaryHeader
import com.example.cointrack.util.calculateNumberOfLoadingRectangles
import com.example.cointrack.util.extensions.shimmerLoadingAnimation
import kotlinx.coroutines.delay

private const val ROW_HEIGHT = 56

@Composable
fun SelectCategoryDialog(
    isDisplayed: Boolean,
    categories: List<String>,
    onDismissed: () -> Unit,
    onCategoryClicked: (String) -> Unit,
    onAddCategoryClicked: () -> Unit,
    isLoading: Boolean,
    isError: String?
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    BottomPopUpDialog(
        isDisplayed = isDisplayed,
        isFixedHeight = true,
        fixedDialogHeight = screenHeight.times(0.95f),
        onCancel = onDismissed
    ) { modifier ->

        Box(
            modifier = modifier.fillMaxSize()
        ) {

            Column {

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                PrimaryHeader(
                    headerTitle = "Select Category",
                    additionalActionIcons = listOf(
                        Pair(painterResource(id = R.drawable.add_item_icon)) { onAddCategoryClicked() }
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))

                if (!isLoading) {

                    LazyVerticalGrid(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {

                        items(categories) { category ->

                            CategoryItem(
                                category = category,
                                onClick = { onCategoryClicked(category) }
                            )
                        }

                        item {

                            Spacer(
                                modifier = Modifier.height(
                                    MaterialTheme.spacing.large +
                                            ComponentSizes.DEFAULT_BUTTON_HEIGHT.dp
                                )
                            )
                        }
                    }

                } else {

                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        IsLoadingCategoriesState(availableHeight = maxHeight)
                    }
                }
            }

            IsErrorCategoryState(isError)
        }
    }
}

@Composable
private fun CategoryItem(
    category: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .height(ROW_HEIGHT.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(Grey85)
            .clickable { onClick() }
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.extraSmall
            ),
        verticalAlignment = CenterVertically
    ) {

        Text(
            text = category,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onSurface,
            maxLines = 1,
            overflow = Ellipsis
        )
    }
}

@Composable
private fun IsLoadingCategoriesState(
    availableHeight: Dp
) {

    val numberOfLoadingRectangles = calculateNumberOfLoadingRectangles(
        availableSpace = availableHeight,
        rectangleHeight = ROW_HEIGHT.dp,
        rectanglePadding = MaterialTheme.spacing.smallMedium
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark)
    ) {

        for (i in 1..numberOfLoadingRectangles) {

            Row(
                modifier = Modifier
                    .padding(
                        bottom = MaterialTheme.spacing.medium
                    )
            ) {

                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .weight(1f)
                        .height(ROW_HEIGHT.dp)
                        .shimmerLoadingAnimation()
                )

                Spacer(Modifier.width(MaterialTheme.spacing.small))

                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .weight(1f)
                        .height(ROW_HEIGHT.dp)
                        .shimmerLoadingAnimation()
                )
            }
        }
    }
}

@Composable
private fun IsErrorCategoryState(
    isError: String?
) {

    isError?.let { error ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Dark),
            contentAlignment = Center
        ) {

            Text(
                text = error,
                style = MaterialTheme.typography.h5,
                color = RedError
            )
        }
    }
}

/** Preview needs to be run on device because onSizeChanged is used in the popup */
@Preview
@Composable
private fun SelectableCategoriesDialogPreview() = CoinTrackTheme {

    val isDialogDisplayed = remember { mutableStateOf(false) }

    val isLoading = remember { mutableStateOf(false) }

    val categories = listOf(
        "Category 1",
        "Category 2",
        "Category 3",
        "Category 4",
        "Category 5",
        "Category 6",
        "Category 7"
    )

    LaunchedEffect(key1 = isDialogDisplayed.value) {

        if (isDialogDisplayed.value) {

            isLoading.value = true
            delay(3000)
            isLoading.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Center
    ) {

        Button(
            onClick = { isDialogDisplayed.value = true }
        ) {

            Text(text = "SHOW POPUP")
        }

        SelectCategoryDialog(
            isDisplayed = isDialogDisplayed.value,
            onDismissed = { isDialogDisplayed.value = false },
            categories = categories,
            onCategoryClicked = { isDialogDisplayed.value = false },
            onAddCategoryClicked = { /* NO ACTION */ },
            isLoading = isLoading.value,
            isError = null
        )
    }
}