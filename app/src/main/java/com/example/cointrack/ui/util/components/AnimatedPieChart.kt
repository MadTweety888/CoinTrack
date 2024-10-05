package com.example.cointrack.ui.util.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cointrack.ui.theme.CoinTrackTheme
import com.example.cointrack.ui.theme.spacing

@Composable
fun AnimatedPieChart(
    data: Map<String, Double>,
    normalisedData: Map<String, Float>,
    radiusOuter: Dp = 140.dp,
    isFilled: Boolean = true,
    chartBarWidth: Dp = 35.dp,
    animDuration: Int = 1000,
) {

    val colors = generateColors(normalisedData.keys.size)

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = "Animated PieChart Size"
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = "Animated PieChart Rotation"
    )

    LaunchedEffect(key1 = Unit) {

        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {

                normalisedData.values.forEachIndexed { index, value ->


                    if (isFilled) {

                        drawArc(
                            color = colors[index],
                            startAngle = lastValue,
                            sweepAngle = value,
                            useCenter = true
                        )

                    } else {

                        drawArc(
                            color = colors[index],
                            startAngle = lastValue,
                            sweepAngle = value,
                            useCenter = false,
                            style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                        )
                    }

                    lastValue += value
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        DetailsPieChart(
            data = data,
            colors = colors
        )
    }
}

@Composable
private fun DetailsPieChart(
    data: Map<String, Double>,
    colors: List<Color>
) {

    LazyColumn {

        itemsIndexed(data.keys.toList()) { index, category ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .border(
                        width = 3.dp,
                        color = colors[index],
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(
                        vertical = MaterialTheme.spacing.small,
                        horizontal = MaterialTheme.spacing.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = category,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onBackground
                )

                Text(
                    text = data[category].toString(),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onBackground
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

/** Preview must be run on device because of used animations */
@Preview
@Composable
private fun AnimatedPieChartPreview() = CoinTrackTheme {

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {

        AnimatedPieChart(
            data = mapOf(
                Pair("Food", 150.0),
                Pair("Health", 43.0),
                Pair("Apparel", 15.0),
                Pair("Avoidable Food", 25.0),
                Pair("Beauty", 34.0),
                Pair("Social Life", 93.0)
            ),
            normalisedData = mapOf(
                Pair("Food", 150f),
                Pair("Health", 43f),
                Pair("Apparel", 15f),
                Pair("Avoidable Food", 25f),
                Pair("Beauty", 34f),
                Pair("Social Life", 93f)
            )
        )
    }

}

private fun generateColors(numberOfValues: Int): List<Color> {
    return List(numberOfValues) { index ->

        Color.hsv(
            hue = (index * 360f / numberOfValues) % 360,
            saturation = 0.7f,
            value = 0.9f
        )
    }
}