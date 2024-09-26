package com.example.cointrack.ui.util.components.bottomnav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes

@Composable
fun BottomNavBar(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    bottomBarState: Boolean,
    onItemClick: (BottomNavItem) -> Unit
) {

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {

        BottomNavigation(
            modifier = modifier
                .height(ComponentSizes.bottomNavBarHeight.dp)
                .clip(MaterialTheme.shapes.large),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 5.dp
        ) {
            items.forEach { item ->

                val selected = item.route == currentRoute

                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onSurface,
                    icon = {
                        Column(
                            horizontalAlignment = CenterHorizontally
                        ) {

                            Icon(
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.spacing.extraSmall),
                                imageVector = item.icon,
                                contentDescription = "BottomBar item icon"
                            )

                            if (selected) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(5.dp)
                                        .background(color = MaterialTheme.colors.primary)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomNavBarVendorPreview() = CoinTrackTheme {

    BottomNavBar(
        currentRoute = "Transactions",
        items = listOf(
            BottomNavItem(
                route = "Transactions",
                icon = Icons.Default.ReceiptLong
            ),
            BottomNavItem(
                route = "Statistics",
                icon = Icons.Default.BarChart
            ),
            BottomNavItem(
                route = "Profile",
                icon = Icons.Default.Person
            )
        ),
        bottomBarState = true,
        onItemClick = {}
    )
}