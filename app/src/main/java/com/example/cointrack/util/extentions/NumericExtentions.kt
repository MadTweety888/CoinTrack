package com.example.cointrack.util.extentions

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Converts raw pixels to density pixels (dp) */
val Int.toDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp