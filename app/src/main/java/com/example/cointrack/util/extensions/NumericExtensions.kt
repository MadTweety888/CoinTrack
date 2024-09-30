package com.example.cointrack.util.extensions

import android.content.res.Resources
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/** Converts raw pixels to density pixels (dp) */
val Int.toDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp

/** Converts text unit size to density pixels (dp) */
fun TextUnit.toDp(density: Density): Dp {

    return with(density) { this@toDp.toDp().value.dp }
}