package com.example.cointrack.util

import androidx.compose.ui.unit.Dp
import kotlin.math.ceil
import kotlin.math.floor

fun calculateNumberOfLoadingRectangles(
    availableSpace: Dp,
    rectangleHeight: Dp,
    rectanglePadding: Dp
): Int {

    return floor(availableSpace.div(rectangleHeight + rectanglePadding * 2)).toInt()
}