@file:OptIn(ExperimentalLayoutApi::class)

package com.example.cointrack.ui.util.components.dialogs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import com.example.cointrack.domain.enums.SortOrder
import com.example.cointrack.domain.enums.SortOrder.DATE_ASCENDING
import com.example.cointrack.domain.enums.SortOrder.DATE_DESCENDING
import com.example.cointrack.domain.enums.TransactionType.EXPENSE
import com.example.cointrack.domain.enums.TransactionType.INCOME
import com.example.cointrack.domain.util.FilterOption
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.Grey20
import com.example.cointrack.ui.theme.Grey30
import com.example.cointrack.ui.theme.Grey85
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.primary.PrimaryButton
import com.example.cointrack.ui.util.primary.PrimaryRadioButton

@Composable
fun <T> SortAndFilterDialog(
    isDisplayed: Boolean,
    isSortToggleable: Boolean = true,
    initialSelectedSort: SortOrder? = null,
    sortOptions: List<SortOrder> = emptyList(),
    initialSelectedFilter: T? = null,
    filterOptions: List<FilterOption<T>> = emptyList(),
    buttonText: String = "Apply",
    onDoneClicked: (selectedSort: SortOrder?, selectedFilter: T?) -> Unit,
    onCancelCTAClicked: () -> Unit,
    cancelDialogCTA: @Composable (onClick: () -> Unit) -> Unit = { DefaultCancelDialogCTA(it) },
    onOutsideOfDialogClicked: () -> Unit
) {

    val selectedSort = remember(key1 = initialSelectedSort) { mutableStateOf(initialSelectedSort) }
    val selectedFilter = remember(key1 = initialSelectedFilter) { mutableStateOf(initialSelectedFilter) }

    BottomPopUpDialog(
        isDisplayed = isDisplayed,
        onCancel = onOutsideOfDialogClicked
    ) { modifier ->

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            if (sortOptions.isNotEmpty()) {

                SortSection(
                    cancelDialogCTA = { cancelDialogCTA(onCancelCTAClicked) },
                    selectedSort = selectedSort.value,
                    sortOptions = sortOptions,
                    onSortOptionClick = {

                        if (isSortToggleable && selectedSort.value == it) {

                            selectedSort.value = null

                        } else {

                            selectedSort.value = it
                        }
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }

            if (filterOptions.isNotEmpty()) {

                FilterSection(
                    shouldDisplayCancelIcon = sortOptions.isEmpty(),
                    cancelDialogCTA = { cancelDialogCTA(onCancelCTAClicked) },
                    selectedFilter = selectedFilter.value,
                    filterOptions = filterOptions,
                    onFilterOptionClick = { selectedFilter.value = it }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }

            ButtonSection(
                buttonText = buttonText,
                onClick = { onDoneClicked(selectedSort.value, selectedFilter.value) }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

//region SortAndFilterDialog Components

@Composable
private fun SortSection(
    cancelDialogCTA: @Composable () -> Unit,
    selectedSort: SortOrder?,
    sortOptions: List<SortOrder>,
    onSortOptionClick: (sortOption: SortOrder) -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .align(Start)
                .fillMaxWidth(),
            horizontalArrangement = SpaceBetween,
            verticalAlignment = CenterVertically
        ) {

            Text(
                text = "Sort",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface
            )


            cancelDialogCTA()
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        sortOptions.forEachIndexed { index, sortOption ->

            Row(
                verticalAlignment = CenterVertically
            ) {

                PrimaryRadioButton(
                    isSelected = selectedSort == sortOption,
                    onClick = { onSortOptionClick(sortOption) }
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = sortOption.toSortOptionString(),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            if (index != sortOptions.size-1) {

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.smallMedium))
            }
        }
    }
}

@Composable
private fun <T> FilterSection(
    shouldDisplayCancelIcon: Boolean,
    cancelDialogCTA: @Composable () -> Unit,
    selectedFilter: T?,
    filterOptions: List<FilterOption<T>>,
    onFilterOptionClick: (filterOptionValue: T) -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .align(Start)
                .fillMaxWidth(),
            horizontalArrangement = SpaceBetween,
            verticalAlignment = CenterVertically
        ) {

            Text(
                text = "Filter",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface
            )

            if (shouldDisplayCancelIcon) {

                cancelDialogCTA()
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(MaterialTheme.spacing.small)
        ) {

            filterOptions.forEach { filterOption ->

                FilterBubble(
                    filterOption = filterOption,
                    isSelected = filterOption.value == selectedFilter,
                    onClick = { onFilterOptionClick(filterOption.value) }
                )
            }
        }
    }
}

@Composable
private fun <T> FilterBubble(
    filterOption: FilterOption<T>,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val bubbleColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primary else Grey85,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "Filter Bubble Color Animation"
    )

    Box(
        modifier = Modifier
            .padding(vertical = MaterialTheme.spacing.extraSmall)
            .clip(CircleShape)
            .background(bubbleColor)
            .clickable { onClick() }
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.extraSmall
            ),
        contentAlignment = Center
    ) {

        Text(
            text = filterOption.name,
            style = MaterialTheme.typography.caption,
            color = if (isSelected) MaterialTheme.colors.onPrimary else Grey30
        )
    }
}

@Composable
private fun ButtonSection(
    buttonText: String,
    onClick: () -> Unit
) {

    PrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = buttonText,
        onClick = onClick
    )
}

//endregion

@Composable
private fun DefaultCancelDialogCTA(onClick: () -> Unit) {

    IconButton(
        onClick = onClick
    ) {

        Icon(
            imageVector = Icons.Default.Close,
            tint = Grey20,
            contentDescription = null
        )
    }
}

/** Preview needs to be run on device because onSizeChanged is used in the popup */
@Preview
@Composable
private fun SortAndFilterDialogPreview() = CoinTrackTheme {

    val isDialogDisplayed = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Center
    ) {

        Button(
            onClick = { isDialogDisplayed.value = true }
        ) {

            Text(text = "SHOW POPUP")
        }

        SortAndFilterDialog(
            isDisplayed = isDialogDisplayed.value,
            initialSelectedSort = null,
            sortOptions = listOf(DATE_ASCENDING, DATE_DESCENDING),
            initialSelectedFilter = null,
            filterOptions = listOf(
                FilterOption("All", null),
                FilterOption("Expenses", EXPENSE),
                FilterOption("Incomes", INCOME)
            ),
            onDoneClicked = { _, _ ->

                isDialogDisplayed.value = false
            },
            onCancelCTAClicked = { isDialogDisplayed.value = false },
            onOutsideOfDialogClicked = { isDialogDisplayed.value = false },
        )
    }
}

@Composable
private fun SortOrder.toSortOptionString(): String {

    return when (this) {

        DATE_ASCENDING  -> "Sort by date ascending"
        DATE_DESCENDING -> "Sort by date descending"
    }
}