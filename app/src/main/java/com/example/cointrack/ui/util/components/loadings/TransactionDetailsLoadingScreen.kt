package com.example.cointrack.ui.util.components.loadings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.spacing

@Composable
fun TransactionDetailsLoadingScreen() {

    DefaultLoadingBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            ShimmeringBox(height = 28.dp)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            ShimmeringBox(height = 38.dp)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            ShimmeringBox(height = 40.dp)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            ShimmeringBox(height = 40.dp)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            ShimmeringBox(height = 40.dp)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            ShimmeringBox(height = 34.dp)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            ShimmeringBox(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
    }
}

@Preview
@Composable
private fun TransactionDetailsLoadingScreenPreview() = CoinTrackTheme {

    TransactionDetailsLoadingScreen()
}