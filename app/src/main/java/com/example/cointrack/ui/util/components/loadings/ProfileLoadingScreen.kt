package com.example.cointrack.ui.util.components.loadings

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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.spacing
import com.example.cointrack.ui.util.ComponentSizes
import com.example.cointrack.util.extensions.shimmerLoadingAnimation

@Composable
fun ProfileLoadingScreen(
    photoSize: Dp
) {

    DefaultLoadingBackground {

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
                        .size(photoSize)
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
                    .height(ComponentSizes.BOTTOM_NAV_BAR_HEIGHT.dp + MaterialTheme.spacing.large)
            )
        }
    }
}

@Preview
@Composable
private fun ProfileLoadingScreenPreview() = CoinTrackTheme {

    ProfileLoadingScreen(photoSize = 120.dp)
}