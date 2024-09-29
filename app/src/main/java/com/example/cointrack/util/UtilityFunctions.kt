package com.example.cointrack.util

import androidx.compose.ui.unit.Dp
import kotlin.math.ceil

fun calculateNumberOfLoadingRectangles(
    availableSpace: Dp,
    rectangleHeight: Dp,
    rectanglePadding: Dp
): Int {

    return ceil(availableSpace.div(rectangleHeight + rectanglePadding * 2)).toInt()
}